package com.example.worker.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true) // 모르는 필드가 있어도 에러 안 나게 설정
data class BookEventDto(
    val type: String,          // "BOOK_ADDED", "BOOK_FINISHED" 등
    val userId: Long,
    val bookId: Long? = null,
    val category: String? = null,
    val totalPages: Int = 0,
    val finishedAt: String? = null // ISO-8601 형식의 날짜 문자열
)
