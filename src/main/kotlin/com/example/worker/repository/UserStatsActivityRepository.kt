package com.example.worker.repository

import com.example.worker.entity.stat.UserStatsActivityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserStatsActivityRepository : JpaRepository<UserStatsActivityEntity, Long> {
    fun findByUserId(userId: Long): UserStatsActivityEntity?
}