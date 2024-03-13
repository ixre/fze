import org.apache.tools.ant.taskdefs.Java
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version ("1.9.0")
    `maven-publish`
    signing
}
//apply("../deploy/deploy.gradle.kts")


dependencies {
    api("javax.inject:javax.inject:1")
    api("com.auth0:java-jwt:4.2.1")
    //api("javax.xml.bind:jaxb-api:2.3.1") //JDK已内置
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("org.aspectj:aspectjweaver:1.9.19")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    compileOnly("com.google.inject:guice:7.0.0")
    compileOnly("com.moandjiezana.toml:toml4j:0.7.2")
    compileOnly("commons-cli:commons-cli:1.5.0")
    compileOnly("com.mchange:c3p0:0.9.5.5") //C3P0-Pool
    compileOnly("io.agroal:agroal-api:1.13")
    compileOnly("io.agroal:agroal-pool:1.13") // Agroal-Pool
    compileOnly("io.etcd:jetcd-core:0.5.11") //only jdk8 = 0.5.11
    compileOnly("redis.clients:jedis:4.2.3")
    compileOnly("io.nats:jnats:2.16.14")
    compileOnly("mysql:mysql-connector-java:8.0.28")
    compileOnly("com.belerweb:pinyin4j:2.5.0")
    // quarkus
    compileOnly("io.quarkus:quarkus-hibernate-orm-panache:1.8.0.Final")
    // spring
    compileOnly("com.baomidou:mybatis-plus-core:3.5.3")
    compileOnly("org.springframework.data:spring-data-redis:2.7.12")

    // grpc
    compileOnly("io.grpc:grpc-stub:1.56.0")
    compileOnly("com.google.protobuf:protobuf-java-util:3.22.3")
    // thrift
    compileOnly("org.apache.thrift:libthrift:0.19.0")
    // etcd
    testCompileOnly("io.etcd:jetcd-core:0.5.4")
    // hibernate
    // hibernate6 使用HQL报错:https://github.com/quarkusio/quarkus/issues/32016
    // api("org.hibernate.orm:hibernate-core:6.4.1.Final")
    // api("org.hibernate.orm:hibernate-c3p0:6.4.1.Final")
    // api("org.hibernate.orm:hibernate-hikaricp:6.4.1.Final")
    // api("org.hibernate.orm:hibernate-jcache:6.4.1.Final")
    compileOnly("org.hibernate:hibernate-core:5.6.15.Final")
    compileOnly("org.hibernate:hibernate-c3p0:5.6.15.Final")
    compileOnly("org.hibernate:hibernate-hikaricp:5.6.15.Final")
    compileOnly("org.hibernate:hibernate-jcache:5.6.15.Final")

    compileOnly("org.junit.jupiter:junit-jupiter:5.7.0")
    testCompileOnly("redis.clients:jedis:4.2.3")
    testCompileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
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

//apply(plugin = "maven-publish")
//apply(plugin ="signing")
//


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.fze"
            artifactId = "fze-commons"
            version = "0.4.8"

            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name = "fze-commons"
                description = "A commons java library"
                url = "http://github.com/ixre/fze"
                properties = mapOf(
                    "myProps" to "value",
                )
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "jarrysix"
                        name = "jarrysix"
                        email = "jarrysix@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/ixre/fze.git"
                    developerConnection = "scm:git:ssh://github.com/ixre/fze.git"
                    url = "http://github.com/ixre/fze"
                }
            }
        }
    }
    repositories {
        // 如果 Sonatype 官方人员给你发的域名不是 s01.oss.sonatype，请把域名替换成官方人员告知你的域名
        val snapshotsRepoUrl =  "https://s01.oss.sonatype.org/content/repositories/snapshots/"
        val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
        maven {
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials{
                username = "jarrysix"
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}
tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

signing {
    sign(publishing.publications["maven"])
}

