plugins {
    java
    kotlin("jvm") version ("1.8.0")
}

//apply("../deploy.gradle")
dependencies {
    implementation(project(":fze-commons"))
    //implementation(kotlin("stdlib"))
    //implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.hibernate:hibernate-core:5.4.2.Final")
    implementation("org.hibernate:hibernate-c3p0:5.4.2.Final")
    implementation("mysql:mysql-connector-java:8.0.18")
    /** Hibernate 依赖项开始 */
    //implementation("javax.activation:activation:1.1.1")
    //implementation("javax.xml.bind:jaxb-api:2.3.1")
    //implementation("com.sun.xml.bind:jaxb-impl:2.3.1")
    //implementation("com.sun.xml.bind:jaxb-core:2.3.1")
    /** Hibernate 依赖项结束 */
    implementation("com.github.brainlag:nsq-client:1.0.0.RC4")
}


