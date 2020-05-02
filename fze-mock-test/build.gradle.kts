plugins {
    java
    kotlin("jvm") version ("1.3.72")
}
dependencies {
    implementation(project(":fze-commons"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.google.inject:guice:4.2.2")
    implementation("redis.clients:jedis:3.2.0")
}
