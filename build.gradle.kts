group = "org.goafabric"
version = "3.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
	java
	jacoco
	id("io.quarkus") version "3.0.0.Beta1"
}

repositories {
	mavenCentral()
}

dependencies {
	constraints {
		implementation("org.mapstruct:mapstruct:1.5.3.Final")
		annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
		testImplementation("org.assertj:assertj-core:3.4.1")
	}

	implementation(enforcedPlatform("io.quarkus:quarkus-bom:3.0.0.Beta1"))
}
dependencies {
	//web
	implementation("io.quarkus:quarkus-arc")
	implementation("io.quarkus:quarkus-resteasy-jackson")
	implementation("org.jboss.logmanager:log4j2-jboss-logmanager")

	//monitoring
	implementation("io.quarkus:quarkus-smallrye-health")
	implementation("io.quarkus:quarkus-smallrye-openapi")
	implementation("io.quarkus:quarkus-smallrye-opentracing")
	implementation("io.quarkus:quarkus-micrometer-registry-prometheus")

	//security
	implementation("io.quarkus:quarkus-elytron-security-properties-file")
	
	//crosscutting
	implementation("io.quarkus:quarkus-hibernate-validator")

	//persistence
	implementation("io.quarkus:quarkus-hibernate-orm-panache")
	implementation("io.quarkus:quarkus-jdbc-postgresql")
	implementation("io.quarkus:quarkus-jdbc-h2")
	implementation("io.quarkus:quarkus-flyway")
	runtimeOnly("com.h2database:h2")

	//adapter
	implementation("io.quarkus:quarkus-rest-client-jackson")
	implementation("io.quarkus:quarkus-smallrye-fault-tolerance")

	//jib
	implementation("io.quarkus:quarkus-container-image-jib")

	//code generation
	implementation("org.mapstruct:mapstruct")
	annotationProcessor("org.mapstruct:mapstruct-processor")

	//test
	testImplementation("io.quarkus:quarkus-junit5")
	testImplementation("io.rest-assured:rest-assured")
	testImplementation("io.quarkus:quarkus-rest-client-jackson")
	testImplementation("io.quarkus:quarkus-jacoco")
}

tasks.withType<Test> {
	dependsOn("quarkusBuild")
	useJUnitPlatform()
	exclude("**/*NRIT*")
	systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

val dockerRegistry = "goafabric"

task<Exec>("dockerImageNative") { dependsOn("quarkusBuild")
	if (gradle.startParameter.taskNames.contains("dockerImageNative")) {
		val archSuffix = if (System.getProperty("os.arch").equals("aarch64")) "-arm64v8" else ""

		if (System.getProperty("os.arch").equals("aarch64")) {
			System.setProperty("quarkus.jib.platforms", "linux/arm64/v8")
		}

		System.setProperty("quarkus.package.type", "native")
		System.setProperty("quarkus.native.container-build", "true")
		System.setProperty("quarkus.container-image.build", "true")
		System.setProperty("quarkus.native.native-image-xmx", "4096m")
		System.setProperty("quarkus.jib.base-native-image", "registry.access.redhat.com/ubi8/ubi-minimal:8.5")
		System.setProperty("quarkus.container-image.image", "${dockerRegistry}/${project.name}${archSuffix}:${project.version}")

		commandLine("docker", "push", "${dockerRegistry}/${project.name}-native${archSuffix}:${project.version}")
		finalizedBy("testNative")
	}
}