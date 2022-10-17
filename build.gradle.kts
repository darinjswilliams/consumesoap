import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.mateo.cxf.codegen.wsdl2java.Wsdl2Java

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	id("io.mateo.cxf-codegen") version "1.0.1"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"

}

group = "com.soap.api"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11


repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web-services")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("javax.xml.ws:jaxws-api:2.3.1")
	implementation ("javax.jws:javax.jws-api:1.1")
	cxfCodegen ("jakarta.xml.ws:jakarta.xml.ws-api:4.0.0")
	cxfCodegen ("jakarta.annotation:jakarta.annotation-api:2.1.0")
	cxfCodegen("ch.qos.logback:logback-classic:1.4.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
configurations.cxfCodegen {
	resolutionStrategy.eachDependency {
		if (requested.group == "org.apache.cxf") {
			useVersion("3.2.0")
			because("3.3.0 breaks the build")
		}
	}
}

tasks.register("customer", Wsdl2Java::class) {
	toolOptions {
		wsdl.set(file("${projectDir}/src/main/resources/wsdl/CustomerOrders.wsdl"))
		outputDir.set(file("${buildDir}/generated-java"))
		serviceName.set("Customer")
		markGenerated.set(true)
		packageNames.set(listOf("com.soap.api"))
		asyncMethods.set(listOf("find"))
	}
	allJvmArgs = listOf("-Duser.language=fr", "-Duser.country=CA")
}



tasks.withType<Test> {
	useJUnitPlatform()
}

