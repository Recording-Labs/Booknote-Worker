package com.example.worker.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "notes")
class NoteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null,
    @Column(name="book_id",nullable = false)
    var bookId: Long,
    @Column(name="user_id",nullable = false)
    var userId: Long,
    var title: String,
    var content: String,
    var html: String,
    @Column(name="is_important", nullable = false)
    var isImportant: Boolean=false,
    @Column(nullable = false)
    var deleted: Boolean = false,
    @Column(name = "created_date")
    var createdDate: LocalDateTime,
    @Column(name = "updated_date")
    var updatedDate: LocalDateTime
)