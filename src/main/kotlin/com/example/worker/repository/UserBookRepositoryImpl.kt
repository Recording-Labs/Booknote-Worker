package com.example.worker.repository

import com.example.worker.entity.QUserBookEntity.userBookEntity
import com.example.worker.entity.QNoteEntity.noteEntity
import com.example.worker.entity.UserBookEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserBookRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserBookRepositoryCustom {

    override fun findBooksByDynamicQuery(userId: Long, category: String?): List<UserBookEntity> {
        return queryFactory
            .selectFrom(userBookEntity)
            .where(
                userBookEntity.userId.eq(userId),
                // 동적 쿼리: category가 null이 아니면 조건 추가
                category?.let { userBookEntity.category.eq(it) }
            )
            .fetch()
    }

    override fun sumTotalPages(userId: Long): Long {
        return (queryFactory
            .select(userBookEntity.totalPages.sum().coalesce(0)) // null이면 0 반환
            .from(userBookEntity)
            .where(
                userBookEntity.userId.eq(userId),
                userBookEntity.deleted.isFalse
            )
            .fetchOne() ?: 0L) as Long
    }

    override fun getReadBooksTotalPages(userId: Long): Long {
        return queryFactory
            .select(userBookEntity.totalPages.sum().coalesce(0)) // null이면 0 반환
            .from(userBookEntity)
            .where(
                userBookEntity.userId.eq(userId)
            )
            .fetchOne()?.toLong() ?: 0L
    }

    override fun getBookCount(userId: Long): Long {
        return queryFactory
            .select(userBookEntity.id.count())
            .from(userBookEntity)
            .where(userBookEntity.userId.eq(userId))
            .fetchOne() ?: 0L
    }

    override fun getNoteCount(userId: Long): Long {
        return queryFactory.select(noteEntity.id.count())
            .from(noteEntity)
            .where(noteEntity.userId.eq(userId))
            .fetchOne() ?: 0L
    }

    override fun getFinishedBookCount(userId: Long): Long {
        return queryFactory
            .select(userBookEntity.id.count())
            .from(userBookEntity)
            .where(
                userBookEntity.userId.eq(userId)
            )
            .fetchOne() ?: 0L
    }

    override fun getMonthlyFinishedCount(userId: Long, start: LocalDateTime, end: LocalDateTime): Long {
        return queryFactory
            .select(userBookEntity.id.count())
            .from(userBookEntity)
            .where(
                userBookEntity.userId.eq(userId),
                userBookEntity.updatedDate.between(start, end) // 기간 필터링
            )
            .fetchOne() ?: 0L
    }

    override fun getMonthlyTotalPages(userId: Long, start: LocalDateTime, end: LocalDateTime): Long {
        return queryFactory
            .select(userBookEntity.totalPages.sum().coalesce(0)) // 페이지 수 합계
            .from(userBookEntity)
            .where(
                userBookEntity.userId.eq(userId),
                userBookEntity.updatedDate.between(start, end)
            )
            .fetchOne()?.toLong() ?: 0L
    }

    override fun getCategoryFinishedCount(userId: Long, categoryCode: String): Long {
        return queryFactory
            .select(userBookEntity.id.count())
            .from(userBookEntity)
            .where(
                userBookEntity.userId.eq(userId),
                userBookEntity.category.eq(categoryCode) // 카테고리 필터링
            )
            .fetchOne() ?: 0L
    }

}