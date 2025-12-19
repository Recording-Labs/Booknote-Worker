package com.example.worker.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
@Entity
@Table(name = "user_books")
class UserBookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "user_id", nullable = false)
    var userId: Long,
    @Column(name = "book_id", nullable = false)
    var bookId: Long,
    @Column(nullable = false)
    var progress: Int = 0,
    var currentPage: Int = 0,
    var totalPages: Int = 0,
    var rating: Int = 0,
    var deleted: Boolean = false,
    var category: String = "",
    @Column(nullable = false)
    var isBookmarked: Boolean = false,
    @Column(name = "created_date")
    var createdDate: LocalDateTime,
    @Column(name = "updated_date")
    var updatedDate: LocalDateTime
)