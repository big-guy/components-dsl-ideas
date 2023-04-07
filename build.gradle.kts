import ng.org.gradle.jvm.model.JvmFeature
import ng.org.gradle.software.model.LibraryComponent
import ng.org.gradle.java.model.JavaSourceSet

plugins {
   id("ng.org.gradle.java-library")
}

library {
   sources {
      "java" {

      }
   }
}

ng {
   ngComponents {
      "foo"(LibraryComponent::class) {
         features {
            "main"(JvmFeature::class) {
               targetJdks.set(listOf(8, 11, 15))
               sources {
                  "java"(JavaSourceSet::class) {
                     srcDir("src/other")
                  }
               }
               dependencies {
                  api("foo:bar:1.0")
                  implementation("bar:foo:2.0")
               }
               targets {
                  // TODO: How do we configure JDK8 only variant?
//                  "main8" {
//                     dependencies {
//                        api("jdk8:only:1.0")
//                     }
//                  }
               }
            }
         }
      }
   }
}
