package com.example.worker.entity.activity

import com.example.worker.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_activities")
class UserActivityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(nullable = false, length = 20)
    var type: String, // "note", "reading", "finished"

    @Column(name = "book_id", nullable = false)
    var bookId: Long,

    @Column(columnDefinition = "TEXT", nullable = true)
    var content: String? = null, // 노트 내용 (type이 "note"인 경우)

    @Column(nullable = true)
    var pages: Int? = null, // 읽은 페이지 수 (type이 "reading"인 경우)

    @Column(nullable = false)
    var deleted: Boolean = false
) : BaseEntity()