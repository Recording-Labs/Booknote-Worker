package com.example.worker.repository

import com.example.worker.entity.UserStatsSummaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserStatsSummaryRepository: JpaRepository<UserStatsSummaryEntity, Long> {
}