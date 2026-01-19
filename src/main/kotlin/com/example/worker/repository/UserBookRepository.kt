package com.example.worker.repository

import com.example.worker.entity.book.UserBookEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserBookRepository : JpaRepository<UserBookEntity, Long>, UserBookRepositoryCustom {
    fun countByUserIdAndDeletedFalse(userId: Long): Long

}