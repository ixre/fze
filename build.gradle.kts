allprojects {
    group = "net.fze"
    version = "0.3.6"
}

subprojects {
    repositories {
        maven("https://maven.aliyun.com/nexus/content/groups/public/")       // manven
        mavenCentral()
    }
    apply(plugin = "java")
    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.8.2")
        "testImplementation"("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
