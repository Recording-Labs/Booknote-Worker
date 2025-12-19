package com.example.worker.repository

import com.example.worker.entity.UserBookEntity
import java.time.LocalDateTime

interface UserBookRepositoryCustom {
    fun findBooksByDynamicQuery(userId: Long, category: String?): List<UserBookEntity>
    fun sumTotalPages(userId: Long): Long
    fun getReadBooksTotalPages(userId: Long): Long
    fun getBookCount(userId: Long): Long
    fun getFinishedBookCount(userId: Long): Long
    fun getMonthlyFinishedCount(userId: Long, start: LocalDateTime, end: LocalDateTime): Long
    fun getMonthlyTotalPages(userId: Long, start: LocalDateTime, end: LocalDateTime): Long
    fun getCategoryFinishedCount(userId: Long, categoryCode: String): Long
    fun getNoteCount(userId: Long): Long
}