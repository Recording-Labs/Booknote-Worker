package com.example.worker.repository

import com.example.worker.entity.note.NoteTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteTagRepository:JpaRepository<NoteTagEntity,Long>, NoteTagRepositoryCustom {
}