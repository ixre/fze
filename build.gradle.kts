allprojects {
    apply(plugin = "java")
    group = "net.fze.arch"
    version="0.1.82"
}

subprojects {
    repositories {
        maven("http://maven.aliyun.com/nexus/content/4/public/")       // manven
        mavenCentral()
    }

    dependencies {
        "implementation"(kotlin("stdlib"))
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.5.1")
        "testImplementation"("org.junit.jupiter:junit-jupiter-engine:5.5.1")
    }
    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
