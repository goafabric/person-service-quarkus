val group: String by project
val version: String by project
java.sourceCompatibility = JavaVersion.VERSION_21

val dockerRegistry = "goafabric"

plugins {
	java
	jacoco
	id("io.quarkus") version "3.22.3"
	id("net.researchgate.release") version "3.1.0"
}

repositories {
	mavenCentral()
}

dependencies {
	constraints {
		implementation("org.mapstruct:mapstruct:1.6.3")
		annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
		testImplementation("org.assertj:assertj-core:3.27.3")
	}

	implementation(enforcedPlatform("io.quarkus:quarkus-bom:3.23.0"))
}
dependencies {
	//web
	implementation("io.quarkus:quarkus-arc")
	implementation("io.quarkus:quarkus-resteasy-jackson")
	implementation("org.jboss.logmanager:log4j2-jboss-logmanager")

	//monitoring
	implementation("io.quarkus:quarkus-smallrye-health")
	implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
	implementation("io.quarkus:quarkus-smallrye-openapi")
	implementation("io.quarkus:quarkus-opentelemetry")
	implementation("io.opentelemetry.instrumentation:opentelemetry-jdbc")

	//crosscutting
	implementation("io.quarkus:quarkus-hibernate-validator")

	//persistence
	implementation("io.quarkus:quarkus-jdbc-postgresql")
	implementation("io.quarkus:quarkus-jdbc-h2")
	implementation("io.quarkus:quarkus-flyway")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("com.h2database:h2")

	implementation("io.quarkus:quarkus-hibernate-orm-panache")
	
	//jakarta data
	implementation("io.quarkus:quarkus-hibernate-orm")
	implementation("jakarta.data:jakarta.data-api:1.0.1")
	annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.6.15.Final")

	//adapter
	implementation("io.quarkus:quarkus-resteasy-client-jackson")
	implementation("io.quarkus:quarkus-smallrye-fault-tolerance")

	//jib
	implementation("io.quarkus:quarkus-container-image-jib")

	//code generation
	implementation("org.mapstruct:mapstruct")
	annotationProcessor("org.mapstruct:mapstruct-processor")

	//test
	testImplementation("io.quarkus:quarkus-junit5")
	testImplementation("io.rest-assured:rest-assured")
	testImplementation("io.quarkus:quarkus-resteasy-client-jackson")
	testImplementation("io.quarkus:quarkus-jacoco")
	testImplementation("org.assertj:assertj-core")
}

tasks.withType<Test> {
	dependsOn("quarkusBuild")
	useJUnitPlatform()
	exclude("**/*NRIT*")
	systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.register<Exec>("dockerImageNative") { group = "build" ; dependsOn("quarkusBuild", "testNative")
	if (gradle.startParameter.taskNames.contains("dockerImageNative")) {
		val archSuffix = if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else ""

		if (System.getProperty("os.arch").equals("aarch64")) {
			System.setProperty("quarkus.jib.platforms", "linux/arm64/v8")
		}

		System.setProperty("quarkus.native.builder-image", "quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21")
		System.setProperty("quarkus.package.jar.enabled", "false")

		System.setProperty("quarkus.native.enabled", "true")
		System.setProperty("quarkus.native.container-build", "true")
		System.setProperty("quarkus.container-image.build", "true")

		System.setProperty("quarkus.native.native-image-xmx", "4096m")
		System.setProperty("quarkus.jib.base-native-image", "registry.access.redhat.com/ubi8/ubi-minimal:8.10")
		System.setProperty("quarkus.container-image.image", "${dockerRegistry}/${project.name}${archSuffix}:${project.version}")

		commandLine("/bin/sh", "-c", "docker push ${dockerRegistry}/${project.name}${archSuffix}:${project.version}")
	}
}

configure<net.researchgate.release.ReleaseExtension> {
	buildTasks.set(listOf("build", "test", "dockerImageNative"))
	tagTemplate.set("v${version}".replace("-SNAPSHOT", ""))
}