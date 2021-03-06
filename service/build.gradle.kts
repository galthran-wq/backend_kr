plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

sourceSets {
    create("intTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

configurations["intTestImplementation"].extendsFrom(configurations["implementation"])
configurations["intTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["intTestRuntimeOnly"].extendsFrom(configurations["runtimeOnly"])
configurations["intTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

idea {
    module {
        testSourceDirs.addAll(sourceSets["intTest"].java.srcDirs)
        testResourceDirs.addAll(sourceSets["intTest"].resources.srcDirs)
        scopes["TEST"]!!["plus"]!!.add(configurations["intTestCompile"])
    }
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation(project(":service-bus"))
    implementation(project(":service-api"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("io.jsonwebtoken:jjwt-api:0.11.2")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    "intTestAnnotationProcessor"("org.projectlombok:lombok")
    "intTestCompileOnly"("org.projectlombok:lombok")
    "intTestImplementation"("org.springframework.cloud:spring-cloud-starter-openfeign:3.0.2")
    "intTestImplementation"("io.github.openfeign:feign-jackson:11.0")
}

tasks {

    register<Test>("integrationTest") {
        description = "Runs the integration tests."
        group = "verification"

        testClassesDirs = sourceSets["intTest"].output.classesDirs
        classpath = sourceSets["intTest"].runtimeClasspath
        shouldRunAfter("test")
    }

    named("check") { dependsOn("integrationTest") }

}