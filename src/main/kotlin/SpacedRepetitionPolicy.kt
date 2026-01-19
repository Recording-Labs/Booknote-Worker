
import java.time.LocalDate

/**
 * 간격 반복(Spaced Repetition) 정책을 나타내는 Value Object
 *
 * 복습 주기:
 * - 1차: 작성일 + 1일
 * - 2차: + 3일
 * - 3차: + 7일
 * - 4차: + 14일
 * - 5차: + 30일 (이후 30일 주기 반복)
 */
object SpacedRepetitionPolicy {
    /**
     * 레벨별 간격 일수
     * 인덱스가 레벨을 나타냄: [0]=1일, [1]=3일, [2]=7일, [3]=14일, [4]=30일
     */
    private val LEVEL_INTERVALS = listOf(1, 3, 7, 14, 30)

    /**
     * 최대 레벨 (5차까지, 0~4)
     */
    val MAX_LEVEL = 4

    /**
     * 특정 레벨의 간격 일수를 반환합니다.
     *
     * @param level 현재 레벨 (0~4)
     * @return 다음 레벨로 가기 위한 간격 일수
     * @throws IllegalArgumentException 레벨이 범위를 벗어난 경우
     */
    fun getIntervalDays(level: Int): Int {
        require(level in 0..MAX_LEVEL) { "레벨은 0~$MAX_LEVEL 사이여야 합니다. 현재: $level" }
        return LEVEL_INTERVALS[level]
    }

    /**
     * 다음 복습일을 계산합니다.
     *
     * @param currentLevel 현재 레벨
     * @param baseDate 기준 날짜 (보통 오늘)
     * @return 다음 복습일
     */
    fun calculateNextReviewDate(currentLevel: Int, baseDate: LocalDate): LocalDate {
        return if (currentLevel < MAX_LEVEL) {
            // 아직 마지막 레벨이 아니면 다음 레벨의 간격 사용
            val nextLevel = currentLevel + 1
            val intervalDays = getIntervalDays(nextLevel)
            baseDate.plusDays(intervalDays.toLong())
        } else {
            // 마지막 레벨(5차) 완료 후: 30일 주기 반복
            baseDate.plusDays(30)
        }
    }

    /**
     * 초기 복습일을 계산합니다 (작성일 기준 다음날 자정부터).
     *
     * @param createdAt 작성일
     * @return 다음날 자정 (1차 복습일)
     */
    fun calculateInitialReviewDate(createdAt: LocalDate): LocalDate {
        return createdAt.plusDays(1)
    }
}
