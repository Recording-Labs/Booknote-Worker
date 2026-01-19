package com.example.worker.repository

import com.example.worker.entity.review.ReviewItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewItemRepository : JpaRepository<ReviewItemEntity, Long> {
}