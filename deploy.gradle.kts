plugins{
    "maven-publish"
}
publishing {
    publications {
        create<MavenPublication>("library") {
            from(components.getByName("java"))
        }
    }
    repositories {
        maven {
            url = "http://git.tech.meizhuli.net:8082/repository/maven-releases"
            credentials(PasswordCredentials::class)
              //  .isAllowInsecureProtocol = true// manven
        }
    }
}

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
