package com.example.worker.service

import com.example.worker.dto.BookEventDto
import com.example.worker.entity.UserStatsCategoryEntity
import com.example.worker.entity.UserStatsMonthlyEntity
import com.example.worker.entity.UserStatsSummaryEntity
import com.example.worker.repository.UserStatsCategoryRepository
import com.example.worker.repository.UserStatsMonthlyRepository
import com.example.worker.repository.UserStatsSummaryRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class UserStatsService(
    private val summaryRepository: UserStatsSummaryRepository,
    private val monthlyRepository: UserStatsMonthlyRepository,
    private val categoryRepository: UserStatsCategoryRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 책 추가 이벤트 처리 (총 권수 +1)
     */
    @Transactional
    fun handleBookAdded(userId: Long) {
        val summary = summaryRepository.findById(userId)
            .orElseGet { UserStatsSummaryEntity(userId = userId) }

        summary.totalBooks += 1
        summaryRepository.save(summary)

        logger.info("✅ User($userId): Book Added Stats Updated")
    }

    /**
     * 책 완독 이벤트 처리 (완독수, 페이지수, 월별, 카테고리 업데이트)
     */
    @Transactional
    fun handleBookFinished(event: BookEventDto) {
        val userId = event.userId

        // 1. 전체 요약 통계 업데이트
        val summary = summaryRepository.findById(userId)
            .orElseGet { UserStatsSummaryEntity(userId = userId) }

        summary.readBooks += 1
        summary.totalPages += event.totalPages
        summaryRepository.save(summary)

        // 2. 월별 통계 업데이트
        // finishedAt이 없으면 오늘 날짜 사용
        val finishedDate = if (event.finishedAt != null) {
            LocalDate.parse(event.finishedAt.substring(0, 10))
        } else {
            LocalDate.now()
        }

        val year = finishedDate.year.toString()
        val month = String.format("%02d", finishedDate.monthValue) // "01", "12" 등

        val monthly = monthlyRepository.findByUserIdAndYearAndMonth(userId, year, month)
            ?: UserStatsMonthlyEntity(userId = userId, year = year, month = month)

        monthly.readCount += 1
        monthly.pageCount += event.totalPages
        monthlyRepository.save(monthly)

        // 3. 카테고리별 통계 업데이트
        if (!event.category.isNullOrEmpty()) {
            val category = categoryRepository.findByUserIdAndCategoryCode(userId, event.category)
                ?: UserStatsCategoryEntity(userId = userId, categoryCode = event.category)

            category.count += 1
            categoryRepository.save(category)
        }

        logger.info("✅ User($userId): Book Finished Stats Updated (Year: $year, Month: $month)")
    }
}