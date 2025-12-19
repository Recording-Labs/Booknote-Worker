package com.example.worker.service

import com.example.worker.dto.BookEventDto
import com.example.worker.entity.UserStatsCategoryEntity
import com.example.worker.entity.UserStatsMonthlyEntity
import com.example.worker.entity.UserStatsSummaryEntity
import com.example.worker.repository.UserBookRepository
import com.example.worker.repository.UserStatsCategoryRepository
import com.example.worker.repository.UserStatsMonthlyRepository
import com.example.worker.repository.UserStatsSummaryRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class UserStatsService(
    private val summaryRepository: UserStatsSummaryRepository,
    private val monthlyRepository: UserStatsMonthlyRepository,
    private val categoryRepository: UserStatsCategoryRepository,
    private val userBookRepository: UserBookRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 책 추가 이벤트 처리 (총 권수 재계산)
     */
    @Transactional
    fun handleBookAdded(userId: Long) {
        // 1. QueryDSL로 실제 DB 카운트 조회 (Source of Truth)
        val realBookCount = userBookRepository.getBookCount(userId)
        val realNoteCount = userBookRepository.getNoteCount(userId)

        // 2. 통계 테이블 조회 및 업데이트 (Upsert)
        val summary = summaryRepository.findById(userId)
            .orElseGet { UserStatsSummaryEntity(userId = userId) }

        // 기존 +1 방식 대신 실제 값으로 덮어쓰기
        summary.totalBooks = realBookCount.toInt()
        summary.totalNotes = realNoteCount.toInt()
        summaryRepository.save(summary)

        logger.info("✅ User($userId): Book Added Stats Recalculated (Total: $realBookCount)")
    }

    /**
     * 책 완독 이벤트 처리 (완독수, 페이지수 재계산)
     */
    @Transactional
    fun handleBookFinished(event: BookEventDto) {
        val userId = event.userId

        // 1. QueryDSL로 실제 DB 데이터 조회 (Source of Truth)
        val realReadCount = userBookRepository.getFinishedBookCount(userId)
        val realTotalPages = userBookRepository.getReadBooksTotalPages(userId)
        // (선택) 완독 시에도 총 권수가 맞는지 한번 더 확인하고 싶다면 아래 주석 해제
        // val realBookCount = userBookQueryDslRepository.getBookCount(userId)

        // 2. 요약 통계 업데이트 (덮어쓰기)
        val summary = summaryRepository.findById(userId)
            .orElseGet { UserStatsSummaryEntity(userId = userId) }

        summary.readBooks = realReadCount.toInt()
        summary.totalPages = realTotalPages.toInt()
        // summary.totalBooks = realBookCount.toInt()

        summaryRepository.save(summary)


        // 3. [유지] 월별/카테고리 통계 (증분 방식)
        // 월별/카테고리까지 전체 재계산하려면 GroupBy 쿼리가 필요하므로, 복잡도를 위해 기존 방식을 유지합니다.
        // 필요하다면 추후 이 부분도 QueryDSL GroupBy로 변경 가능합니다.
        updateMonthlyAndCategoryStats(event)

        logger.info("✅ User($userId): Read Stats Recalculated (Read: $realReadCount, Pages: $realTotalPages)")
    }

    private fun updateMonthlyAndCategoryStats(event: BookEventDto) {
        val userId = event.userId

        // 1. 날짜 및 기간 계산
        val finishedDate = if (event.finishedAt != null) {
            LocalDate.parse(event.finishedAt.substring(0, 10))
        } else {
            LocalDate.now()
        }
        val year = finishedDate.year.toString()
        val month = String.format("%02d", finishedDate.monthValue)

        // 해당 월의 시작과 끝 시간 계산 (예: 2024-05-01 00:00 ~ 2024-05-31 23:59:59)
        val startDateTime = finishedDate.withDayOfMonth(1).atStartOfDay()
        val endDateTime = finishedDate.withDayOfMonth(finishedDate.lengthOfMonth()).atTime(23, 59, 59)


        // --- [A. 월별 통계 재계산] ---
        val realMonthlyReadCount = userBookRepository.getMonthlyFinishedCount(userId, startDateTime, endDateTime)
        val realMonthlyPageCount = userBookRepository.getMonthlyTotalPages(userId, startDateTime, endDateTime)

        val monthly = monthlyRepository.findByUserIdAndYearAndMonth(userId, year, month)
            ?: UserStatsMonthlyEntity(userId = userId, year = year, month = month)

        // 덮어쓰기 (Upsert)
        monthly.readCount = realMonthlyReadCount.toInt()
        monthly.pageCount = realMonthlyPageCount.toInt()
        monthlyRepository.save(monthly)
        logger.info("✅ User($userId): Update Stats Monthly (Read: $realMonthlyReadCount, Pages: $realMonthlyPageCount)")


        // --- [B. 카테고리 통계 재계산] ---
        if (!event.category.isNullOrEmpty()) {
            val realCategoryCount = userBookRepository.getCategoryFinishedCount(userId, event.category)

            val category = categoryRepository.findByUserIdAndCategoryCode(userId, event.category)
                ?: UserStatsCategoryEntity(userId = userId, categoryCode = event.category)

            // 덮어쓰기 (Upsert)
            category.count = realCategoryCount.toInt()
            categoryRepository.save(category)
        }
    }
}