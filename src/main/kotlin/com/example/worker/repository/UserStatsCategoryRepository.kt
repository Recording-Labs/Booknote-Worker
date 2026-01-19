package com.example.worker.repository

import com.example.worker.entity.stat.UserStatsCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserStatsCategoryRepository : JpaRepository<UserStatsCategoryEntity, Long> {
    fun findByUserIdAndCategoryCode(userId: Long, categoryCode: String): UserStatsCategoryEntity?
}