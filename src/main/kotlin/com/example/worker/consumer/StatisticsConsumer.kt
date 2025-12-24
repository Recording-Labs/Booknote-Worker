package com.example.worker.consumer

import com.example.worker.dto.BookEventDto
import com.example.worker.dto.NoteEventDto
import com.example.worker.dto.QuoteEventDto
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

    @KafkaListener(
        topics = ["\${kafka.topic.book-events}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun listen(message: String) {
        try {
            logger.info("📥 Kafka Message Received: $message")

            val event = objectMapper.readValue(message, BookEventDto::class.java)

            when (event.type) {
                "BOOK_ADDED" -> userStatsService.handleBookAdded(event)
                "BOOK_FINISHED" -> userStatsService.handleBookFinished(event)
                else -> logger.warn("⚠️ Unknown event type: ${event.type}")
            }
        } catch (e: Exception) {
            logger.error("❌ Error processing Kafka message", e)
        }
    }

    @KafkaListener(
        topics = ["\${kafka.topic.note-events}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun listenNoteEvents(message: String) {
        try {
            logger.info("📝 Note Event Received: $message")

            val event = objectMapper.readValue(message, NoteEventDto::class.java)

            when (event.type) {
                "NOTE_ADDED" -> userStatsService.handleNoteAdded(event)
                "NOTE_UPDATED" -> userStatsService.handleNoteUpdated(event)
                // "NOTE_DELETED" 등 추후 확장 가능
                else -> logger.warn("⚠️ Unknown Note event type: ${event.type}")
            }
        } catch (e: Exception) {
            logger.error("❌ Error processing Note event", e)
        }
    }

    @KafkaListener(
        topics = ["\${kafka.topic.quote-events}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun listenQuoteEvents(message: String) {
        try {
            logger.info("📝 Note Event Received: $message")

            val event = objectMapper.readValue(message, QuoteEventDto::class.java)

            when (event.type) {
                "QUOTE_ADDED" -> userStatsService.handleQuoteAdded(event)
                "QUOTE_UPDATED" -> userStatsService.handleQuoteUpdated(event)
                // "NOTE_DELETED" 등 추후 확장 가능
                else -> logger.warn("⚠️ Unknown Note event type: ${event.type}")
            }
        } catch (e: Exception) {
            logger.error("❌ Error processing Note event", e)
        }
    }
}