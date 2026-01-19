package com.example.worker.service

import com.example.worker.entity.activity.UserActivityEntity
import com.example.worker.repository.UserActivityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActivityService (
    private val activityRepository: UserActivityRepository,
){
    @Transactional
    fun recordNoteActivity(userId: Long, bookId: Long, content: String? = null) {
        val activity = UserActivityEntity(
            null,
            userId,
            "note",
            bookId,
            content?.take(500), // 최대 500자로 제한
            null,
            false
        )
        activityRepository.save(activity)
    }
}