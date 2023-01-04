plugins {
    id("org.kodein.library.mpp")
}

val kspVersion: String by extra

kodein {
    kotlin {
        add(kodeinTargets.jvm.jvm) {
            main.dependencies {
                implementation("org.objenesis:objenesis:3.2")
                implementation("org.javassist:javassist:3.28.0-GA")
            }
        }
        add(kodeinTargets.native.all)
        add(kodeinTargets.js.ir.js)
    }
}

kodeinUpload {
    name = "mockmp-runtime"
    description = "MocKMP runtime"
}
