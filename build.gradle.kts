plugins {
    kotlin("jvm") version "1.3.61"
    jacoco
    id("io.gitlab.arturbosch.detekt").version("1.3.1")
}

group = "com.eclecticlogic"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
}

repositories {
    jcenter()
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.1.1")
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.assertj:assertj-core:3.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

detekt {
    toolVersion = "1.1.1"
    input = files(
        "src/main/kotlin",
        "src/test/kotlin"
    )
    parallel = false
    config = files("detekt/config.yml")
    buildUponDefaultConfig = true
    baseline = file("detekt/baseline.xml")
    disableDefaultRuleSets = false
    debug = false
    ignoreFailures = false

    reports {
        xml {
            enabled = false
        }
        html {
            enabled = true                                // Enable/Disable HTML report (default: true)
            destination = file("build/reports/detekt.html") // Path where HTML report will be stored (default: `build/reports/detekt/detekt.html`)
        }
        txt {
            enabled = true
            destination = file("build/reports/detekt.txt")
        }
    }
}

tasks {
    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = "0.9".toBigDecimal()
                }
            }
        }
    }

    jacocoTestReport {
        reports {
            html.isEnabled = true
        }
        finalizedBy("jacocoTestCoverageVerification")
    }

    "test"(Test::class) {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            events("passed", "skipped", "failed")
        }
        finalizedBy("jacocoTestReport")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}