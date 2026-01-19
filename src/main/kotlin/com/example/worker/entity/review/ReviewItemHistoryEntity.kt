package com.example.worker.entity.review
import com.example.worker.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "review_item_history")
class ReviewItemHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "review_item_id", nullable = false)
    var reviewItemId: Long,

    @Column(name = "review_id", nullable = false)
    var reviewId: Long,

    @Column(nullable = false)
    var level: Int, // 복습 당시 레벨 이력

    @Column(name = "completed_at")
    var completedAt: LocalDateTime? = null,

    @Column(name = "response")
    @Enumerated(EnumType.STRING)
    var response: ReviewResponseType? = null,

    ) : BaseEntity() {

    /**
     * 복습 이력을 생성합니다.
     * 
     * @param reviewItemId 복습 아이템 ID
     * @param reviewId 복습 세션 ID
     * @param level 이 세션에서 복습한 level
     * @param completedAt 복습 완료 시간
     * @param response 복습 응답
     * @return 새로 생성된 ReviewItemHistoryEntity
     */
    companion object {
        fun create(
            reviewItemId: Long,
            reviewId: Long,
            level: Int,
            completedAt: LocalDateTime? = null,
            response: ReviewResponseType? = null
        ): ReviewItemHistoryEntity {
            return ReviewItemHistoryEntity(
                reviewItemId = reviewItemId,
                reviewId = reviewId,
                level = level,
                completedAt = completedAt,
                response = response
            )
        }
    }
}
