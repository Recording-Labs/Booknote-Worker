package com.example.worker.dto

data class NoteEventDto(
    val type: String,       // "NOTE_CREATED"
    val userId: Long,
    val bookId: Long,
    val noteId: Long,
    val tags: List<String>,
)
