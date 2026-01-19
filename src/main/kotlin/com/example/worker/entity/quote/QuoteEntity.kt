package com.example.worker.entity.quote

import com.example.worker.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "quotes")
class QuoteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null,
    @Column(name="book_id",nullable = false)
    var bookId: Long,
    @Column(name="user_id",nullable = false)
    var userId: Long,
    var content: String,
    var page: Int,
    var memo: String,
    @Column(name="is_important", nullable = false)
    var isImportant: Boolean=false,
    @Column(nullable = false)
    var deleted: Boolean = false,
): BaseEntity()