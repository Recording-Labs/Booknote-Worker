package com.example.worker.consumer

import com.example.worker.dto.BookEventDto
import com.example.worker.dto.NoteEventDto
import com.example.worker.service.UserStatsService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import tools.jackson.module.kotlin.jacksonObjectMapper

@Component
class StatisticsConsumer(
    private val userStatsService: UserStatsService
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val objectMapper = jacksonObjectMapper()

    // ==========================================
    // 1. 책 관련 이벤트 리스너 (기존)
    // ==========================================
    @KafkaListener(
        topics = ["\${kafka.topic.book-events}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun listen(message: String) {
        try {
            logger.info("📥 Kafka Message Received: $message")

            // 1. JSON -> DTO 변환
            val event = objectMapper.readValue(message, BookEventDto::class.java)

            // 2. 이벤트 타입별 로직 수행
            when (event.type) {
                "BOOK_ADDED" -> userStatsService.handleBookAdded(event)
                "BOOK_FINISHED" -> userStatsService.handleBookFinished(event)
                else -> logger.warn("⚠️ Unknown event type: ${event.type}")
            }
        } catch (e: Exception) {
            logger.error("❌ Error processing Kafka message", e)
            // 추후 DLQ(Dead Letter Queue) 처리 등을 고려할 수 있습니다.
        }
    }

    // ==========================================
    // 2. 노트(메모) 관련 이벤트 리스너 (신규 추가)
    // ==========================================
    @KafkaListener(
        topics = ["\${kafka.topic.note-events}"], // 👈 application.yml에 새로 정의 필요
        groupId = "\${spring.kafka.consumer.group-id}" // 같은 그룹 ID를 써도 되고, 분리해도 됨
    )
    fun listenNoteEvents(message: String) {
        try {
            logger.info("📝 Note Event Received: $message")

            // NoteEventDto로 변환 (책 이벤트와 구조가 다를 수 있으므로 분리 추천)
            val event = objectMapper.readValue(message, NoteEventDto::class.java)

            when (event.type) {
                "NOTE_ADDED" -> userStatsService.handleNoteAdded(event)
                // "NOTE_DELETED" 등 추후 확장 가능
                else -> logger.warn("⚠️ Unknown Note event type: ${event.type}")
            }
        } catch (e: Exception) {
            logger.error("❌ Error processing Note event", e)
        }
    }


}