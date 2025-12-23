package com.example.worker.repository

import com.example.worker.entity.UserStatsActivityEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserStatsActivityRepository : JpaRepository<UserStatsActivityEntity, Long> {
    fun findByUserId(userId: Long): UserStatsActivityEntity?
}