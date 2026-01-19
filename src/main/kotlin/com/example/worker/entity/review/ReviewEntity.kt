package com.example.worker.entity.review

import com.example.worker.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "reviews")
class ReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var userId: Long,

    @Column(nullable = false)
    var completed: Boolean = false,

    @Column(name = "planned_time")
    var plannedTime: LocalDateTime,
    @Column(name = "completed_time")
    var completedTime: LocalDateTime? = null,
) : BaseEntity() {
    fun completeIfAllItemsCompleted(reviewItems: List<ReviewItemEntity>): Boolean {
        if (completed) {
            return false
        }
        
        val allCompleted = reviewItems.all { it.isCompleted() }
        
        if (allCompleted) {
            completed = true
            completedTime = LocalDateTime.now()
            return true
        }
        
        return false
    }

    fun isCompleted(): Boolean = completed
}