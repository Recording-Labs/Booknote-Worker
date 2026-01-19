package com.example.worker.entity.review

enum class ReviewStatus {
    ACTIVE,   // 진행 중 (간격 반복 도는 중)
    COMPLETED,// 모든 단계 끝/장기 복습 종료
    CANCELLED // 유저가 중간에 그만하기
}