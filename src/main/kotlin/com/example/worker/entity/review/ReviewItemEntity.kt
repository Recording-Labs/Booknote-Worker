package com.example.worker.entity.review

import com.example.worker.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "review_items")
class ReviewItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var itemType: ReviewItemType,

    // 노트 ID or 인용구 ID
    @Column(name = "item_id", nullable = false)
    var itemId: Long,

    @Column(name = "current_level", nullable = false)
    var currentLevel: Int = 0,

    @Column(name = "last_reviewed_at")
    var lastReviewedAt: LocalDateTime? = null,

    @Column(name = "postpone_count", nullable = false)
    var postponeCount: Int = 0,

    var nextReviewDate: LocalDate? = null,

    ) : BaseEntity() {
    
    /**
     * 간격 반복 알고리즘에 따라 복습을 처리합니다.
     * ReviewResponse에 따라 레벨과 다음 복습일을 조정합니다.
     *
     * @param response 복습 응답 (EASY, NORMAL, DIFFICULT, FORGOT)
     * @param now 현재 시각
     * @return 업데이트된 ReviewItemEntity (fluent interface)
     */
    fun completeReview(@Suppress("UNUSED_PARAMETER") response: ReviewResponseType, now: LocalDateTime = LocalDateTime.now()): ReviewItemEntity {
        val today = now.toLocalDate()

        // 모든 응답 타입에서 레벨은 항상 +1 증가
        if (currentLevel < SpacedRepetitionPolicy.MAX_LEVEL) {
            currentLevel = currentLevel + 1
        }

        // 모든 응답 타입에서 동일한 로직으로 복습 간격 설정 (레벨이 올라가면 간격도 늘어남)
        if (currentLevel >= SpacedRepetitionPolicy.MAX_LEVEL) {
            // 마지막 레벨(5차) 완료 시: 30일 주기 반복
            this.nextReviewDate = SpacedRepetitionPolicy.calculateNextReviewDate(currentLevel, today)
        } else {
            // 현재 레벨에 맞는 간격 사용
            this.nextReviewDate = SpacedRepetitionPolicy.calculateNextReviewDate(currentLevel - 1, today)
        }
        
        lastReviewedAt = now

        return this
    }
    
    /**
     * 복습을 잠시 미룹니다.
     * 레벨은 유지하고 다음 복습일만 변경합니다.
     * 
     * @param snoozeDays 잠시 미룰 일수
     * @param now 현재 날짜
     * @return 업데이트된 ReviewItemEntity (fluent interface)
     */
    fun snooze(snoozeDays: Int, now: LocalDate = LocalDate.now()): ReviewItemEntity {
        require(snoozeDays > 0) { "잠시 미룰 일수는 1 이상이어야 합니다." }
        
        nextReviewDate = now.plusDays(snoozeDays.toLong())
        postponeCount++
        
        return this
    }

    /**
     * 복습 항목이 완료되었는지 확인합니다.
     * 현재 구조에서는 항상 false를 반환합니다.
     * (완료 상태는 ReviewItemHistoryEntity에 기록되거나 별도 로직으로 관리)
     */
    fun isCompleted(): Boolean {
        // 현재 ReviewItemEntity에는 completed 필드가 없으므로
        // 항상 false를 반환합니다.
        // 필요시 lastReviewedAt이나 다른 필드를 기반으로 로직을 추가할 수 있습니다.
        return false
    }
    
    /**
     * 초기 복습 항목을 생성합니다.
     * 
     * @param userId 사용자 ID
     * @param itemType 항목 타입
     * @param itemId 노트/인용구 ID
     * @param createdAt 작성일
     * @return 새로 생성된 ReviewItemEntity
     */
    companion object {
        fun createInitial(
            userId: Long,
            itemType: ReviewItemType,
            itemId: Long,
            createdAt: LocalDate
        ): ReviewItemEntity {
            val nextReviewDate = SpacedRepetitionPolicy.calculateInitialReviewDate(createdAt)
            
            return ReviewItemEntity(
                userId = userId,
                itemType = itemType,
                itemId = itemId,
                currentLevel = 0,
                nextReviewDate = nextReviewDate,
                postponeCount = 0,
            )
        }
    }
}
