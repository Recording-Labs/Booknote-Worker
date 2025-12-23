package com.example.worker.service

import com.example.worker.dto.BookEventDto
import com.example.worker.dto.NoteEventDto
import com.example.worker.entity.UserStatsActivityEntity
import com.example.worker.entity.UserStatsCategoryEntity
import com.example.worker.entity.UserStatsMonthlyEntity
import com.example.worker.entity.UserStatsSummaryEntity
import com.example.worker.entity.UserStatsTagEntity
import com.example.worker.repository.NoteTagRepository
import com.example.worker.repository.UserBookRepository
import com.example.worker.repository.UserStatsActivityRepository
import com.example.worker.repository.UserStatsCategoryRepository
import com.example.worker.repository.UserStatsMonthlyRepository
import com.example.worker.repository.UserStatsSummaryRepository
import com.example.worker.repository.UserStatsTagRepository
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
    private val activityRepository: UserStatsActivityRepository,
    private val tagRepository: UserStatsTagRepository,
    private val noteTagRepository: NoteTagRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun handleBookAdded(event: BookEventDto) {
        val realBookCount = userBookRepository.getBookCount(event.userId)
        val realNoteCount = userBookRepository.getNoteCount(event.userId)

        val summary = summaryRepository.findById(event.userId)
            .orElseGet { UserStatsSummaryEntity(userId = event.userId) }

        summary.totalBooks = realBookCount.toInt()
        summary.totalNotes = realNoteCount.toInt()
        summaryRepository.save(summary)
        updateMonthlyAndCategoryStats(event)

        logger.info("✅ User($event.userId): Book Added Stats Recalculated (Total: $realBookCount)")
    }
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
    @Transactional
     fun updateMonthlyAndCategoryStats(event: BookEventDto) {
        val userId = event.userId

        val finishedDate = if (event.finishedAt != null) {
            LocalDate.parse(event.finishedAt.substring(0, 10))
        } else {
            LocalDate.now()
        }

        val year = finishedDate.year.toString()
        val month = String.format("%02d", finishedDate.monthValue)

        val startDateTime = finishedDate.withDayOfMonth(1).atStartOfDay()
        val endDateTime = finishedDate.withDayOfMonth(finishedDate.lengthOfMonth()).atTime(23, 59, 59)

        val realMonthlyReadCount = userBookRepository.getMonthlyFinishedCount(userId, startDateTime, endDateTime)
        val realMonthlyPageCount = userBookRepository.getMonthlyTotalPages(userId, startDateTime, endDateTime)

        val monthly = monthlyRepository.findByUserIdAndYearAndMonth(userId, year, month)
            ?: UserStatsMonthlyEntity(userId = userId, year = year, month = month)

        monthly.readCount = realMonthlyReadCount.toInt()
        monthly.pageCount = realMonthlyPageCount.toInt()
        monthlyRepository.save(monthly)
        logger.info("✅ User($userId): Update Stats Monthly (Read: $realMonthlyReadCount, Pages: $realMonthlyPageCount)")

        if (!event.category.isNullOrEmpty()) {
            val realCategoryCount = userBookRepository.getCategoryFinishedCount(userId, event.category)

            val category = categoryRepository.findByUserIdAndCategoryCode(userId, event.category)
                ?: UserStatsCategoryEntity(userId = userId, categoryCode = event.category)

            category.count = realCategoryCount.toInt()+1
            categoryRepository.save(category)
        }
    }

    @Transactional
     fun updateActivityStats(userId: Long) {
        val today = LocalDate.now()

        val activityEntity = activityRepository.findByUserId(userId)
            ?: UserStatsActivityEntity(userId = userId)

        activityEntity.updateStreak(today)

        activityRepository.save(activityEntity)
    }

    @Transactional
    fun updateTagStats(userId: Long, tags: List<String>) {
        tags.forEach { tagName ->
            val tagEntity = tagRepository.findByUserIdAndTagName(userId, tagName)
                ?: UserStatsTagEntity(userId = userId, tagName = tagName)

            tagEntity.increment()

            tagRepository.save(tagEntity)
        }
    }

    @Transactional
    fun handleNoteAdded(event: NoteEventDto) {
        val userId = event.userId
        val noteId = event.noteId
        val tags = event.tags

        tags.forEach { tagName ->
            val tagRealCount = noteTagRepository.getTagCount(noteId, tagName)
            val tagEntity = tagRepository.findByUserIdAndTagName(userId, tagName)
                ?: UserStatsTagEntity(userId = userId, tagName = tagName)

            tagEntity.usageCount = tagRealCount.toInt()

            tagRepository.save(tagEntity)
        }
    }
}