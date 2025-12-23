package com.example.worker.repository

import com.example.worker.entity.UserStatsTagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserStatsTagRepository : JpaRepository<UserStatsTagEntity, Long> {
    fun findByUserIdAndTagName(userId: Long, tagName: String): UserStatsTagEntity?
}