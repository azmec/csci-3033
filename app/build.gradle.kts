import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

// Provides arguments to the Room database schema generator.
class RoomSchemaArgProvider(
	@get:InputDirectory
	@get:PathSensitive(PathSensitivity.RELATIVE)
	val schemaDir: File
) : CommandLineArgumentProvider {
	override fun asArguments(): Iterable<String> {
		return listOf("-Aroom.schemaLocation=${schemaDir.path}")
	}
}

plugins {
	id("com.android.application")
}

android {
	namespace = "org.csci.mealmanual"
		compileSdk = 33

		defaultConfig {
			applicationId = "org.csci.mealmanual"
				minSdk = 24
				targetSdk = 33
				versionCode = 1
				versionName = "1.0"

				val apiKey = gradleLocalProperties(rootDir).getProperty("API_KEY");
			buildConfigField("String", "API_KEY", apiKey);

			testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

			// Inform Room to write generated database schemas to the project
			// directory's `schema/` folder.
			javaCompileOptions {
				annotationProcessorOptions {
					compilerArgumentProviders(
						RoomSchemaArgProvider(File(projectDir, "schemas"))
					)
				}
			}
		}

	buildTypes {
		release {
			isMinifyEnabled = false
				proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
			targetCompatibility = JavaVersion.VERSION_1_8
	}
	buildFeatures {
		viewBinding = true
			buildConfig = true
	}

	sourceSets {
		// Make database schemas available for migration testing.
		getByName("androidTest").assets.srcDir("$projectDir/schemas")
	}
}

dependencies {
	constraints {
		val kotlinStdLibVersion = "1.8.0"
			implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinStdLibVersion") {
				because("kotlin-stdlib-jdk8 is now part of kotlin-stdlib")
			}
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinStdLibVersion") {
			because("kotlin-stdlib-jdk7 is now part of kotlin-stdlib")
		}
	}

	val appCompatVersion = "1.6.1"
	implementation("androidx.appcompat:appcompat:$appCompatVersion")

	val materialVersion = "1.8.0"
	implementation("com.google.android.material:material:$materialVersion")

	val constraintLayoutVersion = "2.1.4"
	implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")

	val liveCycleVersion = "2.6.1"
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:$liveCycleVersion")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$liveCycleVersion")
	implementation("androidx.lifecycle:lifecycle-reactivestreams:$liveCycleVersion")

	val navigationVersion = "2.5.3"
	implementation("androidx.navigation:navigation-fragment:$navigationVersion")
	implementation("androidx.navigation:navigation-ui:$navigationVersion")

	val junitVersion = "4.13.2"
	testImplementation("junit:junit:$junitVersion")

	val testJunitVersion = "1.1.5"
	androidTestImplementation("androidx.test.ext:junit:$testJunitVersion")

	val espressoVersion = "3.5.1"
	androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")

	// Dependencies for SQLite integration.
	val roomVersion = "2.5.2"
	implementation("androidx.room:room-runtime:$roomVersion")
	annotationProcessor("androidx.room:room-compiler:$roomVersion")
	androidTestImplementation("androidx.room:room-testing:$roomVersion")

	val rxJavaVersion = "3.1.7";
	val rxJavaAndroidVersion = "3.0.2"
	implementation("io.reactivex.rxjava3:rxjava:$rxJavaVersion")
	implementation("io.reactivex.rxjava3:rxandroid:$rxJavaAndroidVersion")

	val roomRxJavaVersion = "2.5.2"
	implementation("androidx.room:room-rxjava3:$roomRxJavaVersion")

	val retroFitVersion = "2.9.0"
	implementation("com.squareup.retrofit2:retrofit:$retroFitVersion")
	implementation("com.squareup.retrofit2:converter-gson:$retroFitVersion")
	implementation("com.squareup.retrofit2:adapter-rxjava3:$retroFitVersion")

	implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}
