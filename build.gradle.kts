plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains:annotations:19.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
