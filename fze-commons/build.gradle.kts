import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version ("1.8.0")
}
apply("../deploy.gradle")


dependencies {
    //api("javax.xml.bind:jaxb-api:2.3.1")
    api("javax.inject:javax.inject:1")
    api("com.auth0:java-jwt:4.2.1")
    compileOnly("com.zaxxer:HikariCP:5.0.1")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("org.aspectj:aspectjweaver:1.9.19")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    compileOnly("javax.xml.bind:jaxb-api:2.3.1")
    compileOnly("com.google.inject:guice:7.0.0")
    compileOnly("com.moandjiezana.toml:toml4j:0.7.2")
    compileOnly("commons-cli:commons-cli:1.5.0")
    compileOnly("com.mchange:c3p0:0.9.5.5") //C3P0-Pool
    compileOnly("io.agroal:agroal-api:1.13")
    compileOnly("io.agroal:agroal-pool:1.13") // Agroal-Pool
    compileOnly("io.etcd:jetcd-core:0.5.11") //only jdk8 = 0.5.11
    compileOnly("redis.clients:jedis:4.2.3")
    compileOnly("mysql:mysql-connector-java:8.0.28")

    compileOnly("com.belerweb:pinyin4j:2.5.0")
    // quarkus
    compileOnly("io.quarkus:quarkus-hibernate-orm-panache:1.8.0.Final")
    // spring
    compileOnly("com.baomidou:mybatis-plus-core:3.5.3")
    // grpc
    compileOnly("io.grpc:grpc-stub:1.56.0")
    compileOnly("com.google.protobuf:protobuf-java-util:3.22.3")

    testCompileOnly("io.etcd:jetcd-core:0.5.4")
    compileOnly("org.junit.jupiter:junit-jupiter:5.7.0")
    testCompileOnly("redis.clients:jedis:4.2.3")
    testCompileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    compileOnly(kotlin("stdlib-jdk8"))
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}