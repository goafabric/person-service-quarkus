//plugin
id("com.google.devtools.ksp").version("2.3.0")
	
	
val konvertVersion = "4.4.0"
//code generation
implementation("io.mcarle:konvert-api:$konvertVersion")
ksp("io.mcarle:konvert:$konvertVersion")
implementation("io.mcarle:konvert-cdi-annotations:$konvertVersion")
ksp("io.mcarle:konvert-cdi-injector:$konvertVersion")