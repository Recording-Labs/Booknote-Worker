package com.example.worker.entity.stat

import com.example.worker.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_stats_tag")
class UserStatsTagEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "tag_name", nullable = false)
    val tagName: String, // 예: "인문학", "개발", "감동"

    @Column(name = "usage_count")
    var usageCount: Int = 0 // 해당 태그 사용 횟수
) : BaseEntity() {

    fun increment() {
        this.usageCount += 1
    }
}