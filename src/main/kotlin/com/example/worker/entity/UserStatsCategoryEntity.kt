package com.example.worker.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table(name = "user_stats_category")
class UserStatsCategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "category_code", nullable = false, length = 20)
    val categoryCode: String,

    @Column(name = "count")
    var count: Int = 0
) {
    protected constructor() : this(null, 0L, "")

    fun increaseCount() {
        this.count += 1
    }
}