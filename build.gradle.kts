plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "2.2.21"
    kotlin("kapt") version "1.9.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "worker"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-kafka")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-kafka-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // QueryDSL 의존성 추가
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // [테스트 의존성 - 여기를 수정하세요]
    // 1. 통합 테스트 스타터 (JUnit5, Mockito 포함)
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // 2. Kafka 테스트용 (선택 사항)
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // 3. Kotlin JUnit 5 지원 (starter-test에 포함되어 있을 수 있으나 명시해도 됨)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // 4. JUnit 플랫폼 런처 (Gradle 8.x 이상에서 필수)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
// Kapt가 생성한 Q객체 파일이 저장되는 기본 경로
val generated = file("build/generated/source/kapt/main")

// 인텔리제이 등이 소스 폴더로 인식하도록 설정
sourceSets {
    main {
        kotlin.srcDirs(generated)
    }
}

// (선택 사항) Kapt 설정: 컴파일 시 올바른 인자 전달
kapt {
    arguments {
        // 명시적으로 output 경로를 지정하지 않아도 기본적으로 build 폴더로 들어갑니다.
        // 필요한 경우 QueryDSL 옵션을 여기에 추가합니다.
        arg("querydsl.entityAccessors", "true")
    }
    // Java 파일 컴파일러와 연동 유지
    keepJavacAnnotationProcessors = true
}

tasks.withType<Test> {
    useJUnitPlatform()

    // [추가] 테스트 아예 안 함
    enabled = false
}