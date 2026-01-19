package com.example.worker.entity.note

import com.example.worker.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "note_tags")
class NoteTagEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null,
    @Column(name="note_id",nullable = false)
    var noteId: Long,
    @Column(name="tag_id",nullable = false)
    var tagId: Long?,
): BaseEntity()