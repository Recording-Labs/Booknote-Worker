package com.example.worker.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "user_stats_summary")
@EntityListeners(AuditingEntityListener::class)
class UserStatsSummaryEntity(
    @Id
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "total_books")
    var totalBooks: Int = 0,
    @Column(name = "read_books")
    var readBooks: Int = 0,
    @Column(name = "total_pages")
    var totalPages: Int = 0,
    @Column(name = "total_notes")
    var totalNotes: Int = 0,
) {
    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    // JPA를 위한 기본 생성자 (Protected)
    protected constructor() : this(0L)

    // --- 비즈니스 로직 메서드 ---

    // 책 추가 시
    fun increaseTotalBooks() {
        this.totalBooks += 1
    }

    // 책 완독 시
    fun recordReadBook(pages: Int) {
        this.readBooks += 1
        this.totalPages += pages
    }

    // 노트 작성 시
    fun increaseTotalNotes() {
        this.totalNotes += 1
    }
}