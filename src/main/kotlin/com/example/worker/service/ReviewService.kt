package com.example.worker.service

import com.example.worker.entity.review.ReviewItemEntity
import com.example.worker.entity.review.ReviewItemType
import com.example.worker.repository.ReviewItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ReviewService(
    private val reviewItemRepository: ReviewItemRepository,
){

    /**
     * 노트 또는 인용구 생성 시 복습 항목을 생성합니다.
     * ReviewItemEntity만 생성하고, ReviewEntity는 복습 시작 시점에 생성됩니다.
     *
     * @param userId 사용자 ID
     * @param itemType 노트(NOTE) 또는 인용구(QUOTE)
     * @param itemId 노트/인용구 ID
     * @param createdAt 노트/인용구 작성일 (복습 스케줄 계산 기준)
     */
    @Transactional
    fun createReviewItemForContent(
        userId: Long,
        itemType: ReviewItemType,
        itemId: Long,
        createdAt: LocalDateTime
    ) {
        // 기존 ReviewItem이 있는지 확인 (중복 생성 방지)
        val existingItem = reviewItemRepository.findAll().find {
            it.userId == userId && it.itemType == itemType && it.itemId == itemId
        }

        if (existingItem != null) {
            // 이미 복습 항목이 존재하면 생성하지 않음
            return
        }

        // 도메인 팩토리 메서드를 통한 ReviewItemEntity 생성
        // nextReviewDate는 작성일 기준 다음날로 설정됨
        val createdAtDate = createdAt.toLocalDate()
        val reviewItem = ReviewItemEntity.createInitial(
            userId = userId,
            itemType = itemType,
            itemId = itemId,
            createdAt = createdAtDate
        )

        reviewItemRepository.save(reviewItem)
    }

}