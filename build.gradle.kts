plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("libs/poseidon.jar"))
    implementation(files("libs/Permissions.jar"))
    implementation(files("libs/bpermissions.jar"))
    implementation(files("libs/PermissionsBukkit.jar"))
    implementation(files("libs/PermissionsEx.jar"))
    implementation(files("libs/BOSEconomy.jar"))
    implementation(files("libs/iConomy.jar"))
}