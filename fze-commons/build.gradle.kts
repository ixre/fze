plugins {
    java
    kotlin("jvm") version ("1.6.0")
}
apply("../deploy.gradle")


dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    //api("javax.xml.bind:jaxb-api:2.3.1")
    api("javax.inject:javax.inject:1")
    api("com.google.code.gson:gson:2.8.9")
    api("com.zaxxer:HikariCP:5.0.0")
    compileOnly("javax.servlet:javax.servlet-api:4.+")
    compileOnly("javax.xml.bind:jaxb-api:2.3.1")
    compileOnly("com.google.inject:guice:4.2.3")
    compileOnly("com.moandjiezana.toml:toml4j:0.7.2")
    compileOnly("io.agroal:agroal-api:1.9")
    compileOnly("commons-cli:commons-cli:1.4")
    compileOnly("com.mchange:c3p0:0.9.5.5") //C3P0-Pool
    compileOnly("io.agroal:agroal-pool:1.13") // Agroal-Pool
    compileOnly("org.apache.thrift:libthrift:0.13.0")
    compileOnly("io.etcd:jetcd-core:0.5.4")
    compileOnly("redis.clients:jedis:3.3.0")
    compileOnly("mysql:mysql-connector-java:8.0.18")
    compileOnly("com.github.dozermapper:dozer-core:6.5.2")
    compileOnly("com.belerweb:pinyin4j:2.5.0")
    compileOnly("com.esotericsoftware:reflectasm:1.11.9")
    // quarkus
    compileOnly("io.quarkus:quarkus-hibernate-orm-panache:1.8.0.Final")
    testImplementation("io.etcd:jetcd-core:0.5.4")
    testImplementation("redis.clients:jedis:3.3.0")
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
