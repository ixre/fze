apply(plugin = "maven-publish")
apply(plugin = "signing")

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.fze"
            artifactId = "fze-commons"
            version = "0.4.7"

            from(components["java"])
        }
    }
    repositories {
        val snapshotsRepoUrl = "surl"
        val releasesRepoUrl = "rurl"
        maven {
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            credentials{
                username = "username"
                password = "password"
            }
        }
    }
}