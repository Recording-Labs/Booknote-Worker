package com.example.worker.repository

import com.example.worker.entity.NoteTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteTagRepository:JpaRepository<NoteTagEntity,Long>, NoteTagRepositoryCustom {
}