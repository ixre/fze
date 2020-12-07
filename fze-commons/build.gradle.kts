plugins {
    java
    kotlin("jvm") version ("1.4.20")
}
apply("../deploy.gradle")


dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    //implementation()
    //api("javax.xml.bind:jaxb-api:2.3.1")
    compileOnly("javax.servlet:javax.servlet-api:4.+")
    compileOnly("javax.xml.bind:jaxb-api:2.3.1")
    compileOnly("com.google.inject:guice:4.2.3")
    api("com.moandjiezana.toml:toml4j:0.7.2")
    api("io.agroal:agroal-pool:1.9" )
    compileOnly("io.agroal:agroal-api:1.9")
    compileOnly("commons-cli:commons-cli:1.4")
    api("com.google.code.gson:gson:2.8.6")
    compileOnly("com.mchange:c3p0:0.9.5.5") //C3P0连接池
    compileOnly("org.apache.thrift:libthrift:0.13.0")
    compileOnly("io.etcd:jetcd-core:0.5.3")
    compileOnly("redis.clients:jedis:3.3.0")
    compileOnly("mysql:mysql-connector-java:8.0.18")
    compileOnly("com.github.dozermapper:dozer-core:6.5.0")
    compileOnly("com.belerweb:pinyin4j:2.5.0")
    compileOnly("com.esotericsoftware:reflectasm:1.11.9")
    // quarkus
    compileOnly("io.quarkus:quarkus-hibernate-orm-panache:1.8.0.Final")
    testCompileOnly("io.etcd:jetcd-core:0.5.3")
    testImplementation( "redis.clients:jedis:3.3.0")
    testCompileOnly("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}