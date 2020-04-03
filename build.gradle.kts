import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val prometheusVersion = "0.8.1"
val ktorVersion = "1.3.1"
val junitVersion = "5.4.1"
val confluentVersion = "5.2.0"
val logstashVersion = 5.2
val logbackVersion = "1.2.3"
val vaultJdbcVersion = "1.3.1"
val hikariCPVersion = "3.2.0"
val postgresVersion = "42.2.5"
val h2Version = "1.4.199"
val jacksonVersion = "2.9.9"
val kluentVersion = "1.52"
val mockkVersion = "1.9.3"
val jjwtVersion = "0.11.0"
val bcproVersion = "1.64"
val brukernotifikasjonSchemaVersion = "1.2020.02.07-13.16-fa9d319688b1"

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    val kotlinVersion = "1.3.50"
    kotlin("jvm").version(kotlinVersion)
    kotlin("plugin.allopen").version(kotlinVersion)

    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    maven("http://packages.confluent.io/maven")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("no.nav:vault-jdbc:$vaultJdbcVersion")
    compile("com.zaxxer:HikariCP:$hikariCPVersion")
    compile("org.postgresql:postgresql:$postgresVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    compile("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    compile("io.prometheus:simpleclient:$prometheusVersion")
    compile("io.prometheus:simpleclient_hotspot:$prometheusVersion")
    compile("io.prometheus:simpleclient_common:$prometheusVersion")
    compile("io.prometheus:simpleclient_logback:$prometheusVersion")
    compile("io.prometheus:simpleclient_httpserver:$prometheusVersion")
    compile("io.ktor:ktor-auth:$ktorVersion")
    compile("io.ktor:ktor-auth-jwt:$ktorVersion")
    compile("io.ktor:ktor-jackson:$ktorVersion")
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testCompile(kotlin("test-junit5"))
    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testCompile("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    testRuntime("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    testRuntime("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
    testRuntime("org.bouncycastle:bcprov-jdk15on:$bcproVersion")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
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
