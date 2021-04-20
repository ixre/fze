allprojects {
    apply(plugin = "java")
    group = "net.fze"
    version="0.3.0"
}

subprojects {
    repositories {
        maven("https://maven.aliyun.com/nexus/content/groups/public/")       // manven
        mavenCentral()
    }

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.7.0")
        "testImplementation"("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    }
    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
