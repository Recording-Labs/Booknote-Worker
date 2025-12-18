package com.example.worker.repository

import com.example.worker.entity.UserStatsMonthlyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserStatsMonthlyRepository : JpaRepository<UserStatsMonthlyEntity, Long> {
    fun findByUserIdAndYearAndMonth(userId: Long, year: String, month: String): UserStatsMonthlyEntity?
}