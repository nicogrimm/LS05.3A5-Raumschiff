plugins {
    id("java")
    id("application")
}

group = "com.github.nicogrimm"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.github.nicogrimm.Sonnensystem"
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`;
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks.test {
    useJUnitPlatform()
}