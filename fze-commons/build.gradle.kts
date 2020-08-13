plugins {
    java
    kotlin("jvm") version ("1.3.72")
}

apply("../deploy.gradle")
dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
    api("javax.servlet:javax.servlet-api:4.+")
    api("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.google.inject:guice:4.2.3")
    api("io.agroal:agroal-pool:1.8")
    implementation("io.agroal:agroal-api:1.8")
    implementation("commons-cli:commons-cli:1.4")
    api("com.google.code.gson:gson:2.8.6")
    implementation("com.mchange:c3p0:0.9.5.5") //C3P0连接池
    api("org.apache.thrift:libthrift:0.13.0")
    implementation("redis.clients:jedis:3.3.0")
    api("com.moandjiezana.toml:toml4j:0.7.2")
    //"org.apache.commons:commons-lang3:3.7"
    implementation("mysql:mysql-connector-java:8.0.18")
    implementation("com.github.dozermapper:dozer-core:6.5.0")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.belerweb:pinyin4j:2.5.0")
    implementation("com.esotericsoftware:reflectasm:1.11.9")

    // quarkus
    implementation("io.quarkus:quarkus-hibernate-orm-panache:1.4.2.Final")
    testCompileOnly("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
}