allprojects {
    group = "net.fze"
    version="0.3.5"
}

subprojects {
    repositories {
        maven("https://maven.aliyun.com/nexus/content/groups/public/")       // manven
        mavenCentral()
    }
    apply(plugin = "java")
    dependencies {
        "testCompileOnly"("org.junit.jupiter:junit-jupiter-api:5.7.0")
        "testCompileOnly"("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    }
    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
