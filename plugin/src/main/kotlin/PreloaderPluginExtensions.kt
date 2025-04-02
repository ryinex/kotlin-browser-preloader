import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

interface PreloaderPluginExtensions {
    val jsModuleName: Property<String>
    val distributionPath: Property<String>
    val css: RegularFileProperty
    val html: RegularFileProperty
    val logo: RegularFileProperty
    val lengthHeader: Property<String>
    val logEnabled: Property<Boolean>
}