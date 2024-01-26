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

//
//java {
//    withSourcesJar()
//    withJavadocJar()
//}
//
//publishing {
//    publications {
//        mavenJava(MavenPublication) {
//            from components.java
//                    artifactId archivesBaseName
//
//                    pom {
//                        name = 'Console Game Engine'    // 项目名称
//                        description = 'A console game engine for the JVM.'  // 项目简介
//                        url = 'https://github.com/EmptyDreams/ConsoleGameEngine'    // 项目主页
//
//                        scm {   // 版本控制的相关信息，参考我填写的内容
//                            url = 'https://github.com/EmptyDreams/ConsoleGameEngine'
//                            connection = 'scm:git:git:github.com/EmptyDreams/ConsoleGameEngine.git'
//                            developerConnection = 'scm:git:ssh://git@github.com:EmptyDreams/ConsoleGameEngine.git'
//                        }
//
//                        licenses {
//                            license {
//                                name = 'The Affero General Public License, Version 3.0' // 协议名称
//                                url = 'https://www.gnu.org/licenses/agpl-3.0.en.html'   // 协议官网
//                            }
//                        }
//
//                        developers {
//                            developer {
//                                id = 'Kmar'                 // 作者 id
//                                name = '空梦'                 // 作者名称
//                                email = 'minedreams@qq.com' // 作者邮箱
//                            }
//                        }
//                    }
//        }
//    }
//
//    repositories {
//        maven {
//            name = archivesBaseName
//
//            // 如果 Sonatype 官方人员给你发的域名不是 s01.oss.sonatype，请把域名替换成官方人员告知你的域名
//            def releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//            def snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
//            url = version.endsWith('SNAPSHOT') ? snapshotsRepoUrl : releasesRepoUrl
//
//            credentials {
//                username ossrhUsername
//                        password ossrhPassword
//            }
//        }
//    }
//}
//
//signing {
//    sign publishing.publications.mavenJava
//}


//publishing {
//    publications {
//        create<MavenPublication>("library") {
//            from(components.getByName("java"))
//        }
//    }
//    repositories {
//        maven {
//            url = "http://git.tech.meizhuli.net:8082/repository/maven-releases"
//            credentials(PasswordCredentials::class)
//            //  .isAllowInsecureProtocol = true// manven
//        }
//    }
//}
//
//publishing {
//    publications {
//        maven(MavenPublication) {
//            from components . java
//        }
//    }
//}
//   repositories {
//        maven {
//            url = "http://git.tech.meizhuli.net:8082/repository/maven-releases"
//            credentials {
//                username 'line'
//                password "line888"
//            }
//        }
/*
 maven {

     name = "clojars"
     url = "https://repo.clojars.org"
     credentials {
         username="jarrysix"
         password="CLOJARS_b72343bcf0d5729ba55ced4ca5bb783e12eede25e8f97ac0f09f18121fae"
     }
 }
}
}*/
