rootProject.name = "FastAsyncWorldEdit"

include("worldedit-libs")

listOf("1_20_2", "1_20_4", "1_20_5", "1_21").forEach {
    include("worldedit-bukkit:adapters:adapter-$it")
}

listOf("bukkit", "core", "cli").forEach {
    include("worldedit-libs:$it")
    include("worldedit-$it")
}
include("worldedit-libs:core:ap")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            name = "FabricMC"
            url = uri("https://maven.fabricmc.net/")
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
