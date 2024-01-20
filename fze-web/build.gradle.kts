import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version ("1.9.0")
}

//apply("../deploy.gradle")
dependencies {
    implementation(project(":fze-commons"))
    api("org.freemarker:freemarker:2.3.28")
    api("commons-fileupload:commons-fileupload:1.3.3")
    api("javax.servlet:javax.servlet-api:4.0.1")
    api("org.eclipse.jetty:jetty-server:9.4.15.v20190215")
    implementation(kotlin("stdlib-jdk8"))
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