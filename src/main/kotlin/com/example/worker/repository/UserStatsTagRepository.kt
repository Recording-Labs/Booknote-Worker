package com.example.worker.repository

import com.example.worker.entity.stat.UserStatsTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserStatsTagRepository : JpaRepository<UserStatsTagEntity, Long> {
    fun findByUserIdAndTagName(userId: Long, tagName: String): UserStatsTagEntity?
}