package com.example.worker.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "user_stats_monthly")
class UserStatsMonthlyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "year", nullable = false, length = 7)
    val year: String,

    @Column(name = "month", nullable = false, length = 7)
    val month: String,

    @Column(name = "read_count")
    var readCount: Int = 0,

    @Column(name = "page_count")
    var pageCount: Int = 0
) : BaseEntity() {
    // JPA 기본 생성자
    protected constructor() : this(null, 0L, "", "")

    // 통계 업데이트 메서드
    fun addReadData(pages: Int) {
        this.readCount += 1
        this.pageCount += pages
    }
}