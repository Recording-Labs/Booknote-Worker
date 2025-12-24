package com.example.worker.dto

data class QuoteEventDto(
    val type: String,
    val userId: Long,
    val bookId: Long,
    val quoteId: Long,
)
