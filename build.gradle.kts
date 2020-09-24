import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    val kotlinVersion = "1.3.50"
    kotlin("jvm").version(kotlinVersion)
    kotlin("plugin.allopen").version(kotlinVersion)

    id(Shadow.pluginId) version Shadow.version

    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

repositories {
    jcenter()
    maven("http://packages.confluent.io/maven")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(Hikari.cp)
    implementation(Jackson.dataTypeJsr310)
    implementation(Ktor.auth)
    implementation(Ktor.authJwt)
    implementation(Ktor.jackson)
    implementation(Ktor.serverNetty)
    implementation(Logback.classic)
    implementation(Logstash.logbackEncoder)
    implementation(NAV.vaultJdbc)
    implementation(Postgresql.postgresql)
    implementation(Prometheus.simpleClient)
    implementation(Prometheus.hotspot)
    implementation(Prometheus.common)
    implementation(Prometheus.logback)
    implementation(Prometheus.httpServer)
    testImplementation(kotlin("test-junit5"))
    testImplementation(Bouncycastle.bcprovJdk15on)
    testImplementation(H2Database.h2)
    testImplementation(Jjwt.api)
    testImplementation(Jjwt.impl)
    testImplementation(Jjwt.jackson)
    testImplementation(Junit.api)
    testImplementation(Junit.engine)
    testImplementation(Kluent.kluent)
    testImplementation(Mockk.mockk)
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    register("runServer", JavaExec::class) {
        environment("OIDC_ISSUER", "http://localhost:9000")
        environment("OIDC_DISCOVERY_URL", "http://localhost:9000/.well-known/openid-configuration")
        environment("OIDC_ACCEPTED_AUDIENCE", "stubOidcClient")
        environment("OIDC_CLAIM_CONTAINING_THE_IDENTITY", "pid")

        environment("DB_HOST", "localhost:5432")
        environment("DB_NAME", "dittnav-event-cache-preprod")
        environment("DB_PASSWORD", "testpassword")
        environment("DB_MOUNT_PATH", "notUsedOnLocalhost")

        main = application.mainClassName
        classpath = sourceSets["main"].runtimeClasspath
    }
}

apply(plugin = Shadow.pluginId)