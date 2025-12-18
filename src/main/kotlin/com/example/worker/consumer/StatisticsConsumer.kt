package com.example.worker.consumer

import com.example.worker.dto.BookEventDto
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

    // application.yml에 정의된 변수를 SpEL로 가져옵니다.
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
                "BOOK_ADDED" -> userStatsService.handleBookAdded(event.userId)
                "BOOK_FINISHED" -> userStatsService.handleBookFinished(event)
                else -> logger.warn("⚠️ Unknown event type: ${event.type}")
            }
        } catch (e: Exception) {
            logger.error("❌ Error processing Kafka message", e)
            // 추후 DLQ(Dead Letter Queue) 처리 등을 고려할 수 있습니다.
        }
    }
}