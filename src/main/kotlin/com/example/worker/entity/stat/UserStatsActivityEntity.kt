package com.example.worker.entity.stat

import com.example.worker.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "user_stats_activity")
class UserStatsActivityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false, unique = true)
    val userId: Long,

    @Column(name = "current_streak")
    var currentStreak: Int = 0, // 현재 연속 일수

    @Column(name = "max_streak")
    var maxStreak: Int = 0, // 역대 최대 연속 일수

    @Column(name = "last_activity_date")
    var lastActivityDate: LocalDate? = null, // 마지막으로 활동(독서/메모)한 날짜

    @Column(name = "total_read_time_minutes")
    var totalReadTimeMinutes: Long = 0 // (선택) 총 독서 시간 누적
) : BaseEntity() {

    // 스트릭 업데이트 로직
    fun updateStreak(activityDate: LocalDate) {
        if (lastActivityDate == activityDate) return // 오늘 이미 기록함

        if (lastActivityDate == activityDate.minusDays(1)) {
            // 어제 기록이 있으면 +1
            currentStreak += 1
        } else {
            // 끊겼으면 1로 초기화
            currentStreak = 1
        }

        // 최대 스트릭 갱신
        if (currentStreak > maxStreak) {
            maxStreak = currentStreak
        }

        lastActivityDate = activityDate
    }
}