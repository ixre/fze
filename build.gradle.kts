allprojects {
    apply(plugin = "java")
    group = "net.fze"
    version="0.3.0"
}

subprojects {
    repositories {
        maven("http://maven.aliyun.com/nexus/content/groups/public/")       // manven
        mavenCentral()
    }

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api")
        "testImplementation"("org.junit.jupiter:junit-jupiter-engine")
    }
    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}