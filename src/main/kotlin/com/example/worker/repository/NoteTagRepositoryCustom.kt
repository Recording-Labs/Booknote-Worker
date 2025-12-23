package com.example.worker.repository

interface NoteTagRepositoryCustom {
    fun getTagCount(noteId: Long,tagName: String): Long
}