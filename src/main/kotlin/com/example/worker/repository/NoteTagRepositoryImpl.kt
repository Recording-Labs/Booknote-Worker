package com.example.worker.repository

import com.example.worker.entity.QNoteTagEntity.noteTagEntity
import com.example.worker.entity.QTagEntity.tagEntity
import com.example.worker.entity.QUserBookEntity.userBookEntity
import com.querydsl.core.QueryFactory
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class NoteTagRepositoryImpl (
    private val queryFactory: JPAQueryFactory,
): NoteTagRepositoryCustom {

    override fun getTagCount(noteId: Long,tagName: String): Long {
        return queryFactory
            .select(noteTagEntity.id.count())
            .from(noteTagEntity)
            .join(tagEntity).on(noteTagEntity.tagId.eq(tagEntity.id))
            .where(noteTagEntity.noteId.eq(noteId)
                .and(tagEntity.name.eq(tagName)))
            .fetchOne() ?: 0L
    }
}