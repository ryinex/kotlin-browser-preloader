import files.preloadJs
import files.preloaderCss
import files.preloaderHtml
import files.preloaderJs
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

abstract class PreloaderTask : DefaultTask() {
    @get:Input
    abstract val outputDistributionDir: Property<String>

    @get:Input
    abstract val jsModuleName: Property<String>

    @get:Optional
    @get:InputFile
    abstract val css: RegularFileProperty

    @get:Optional
    @get:InputFile
    abstract val html: RegularFileProperty

    @get:Optional
    @get:InputFile
    abstract val logo: RegularFileProperty

    @get:Optional
    @get:Input
    abstract val lengthHeader: Property<String>

    @TaskAction
    fun run() {
        val logoErrorMessage = """
            Please provide an svg logo image for the preloader like this:
            preloader {
                logo.set(project.file("<image path>"))
            }
        """.trimIndent()
        val moduleNameError = """
            Please provide a module name for the preloader like this:
            preloader {
                jsModuleName.set("<module name>")
            }
        """.trimIndent()
        val path = outputDistributionDir.get()
        val moduleName = jsModuleName.orNull ?: error(moduleNameError)
        val html = html.orNull
        val css = css.orNull
        val logo = if (html == null) {
            logo.getOrElse { error(logoErrorMessage) }
        } else {
            logo.orNull
        }

        val header = lengthHeader.getOrElse("x-decompressed-content-length")

        (html?.asFile ?: preloaderHtml(logo!!.asFile.name).file()).copy("$path", "preloader.html")
        (css?.asFile ?: preloaderCss.file()).copy("$path", "preloader.css")
        logo?.asFile?.copy("$path", logo.asFile.name)

        preloadJs(header).file().copy("$path", "Preload.js")
        preloaderJs(moduleName).file().copy("$path", "preloader.js")

        script(path)
    }

    private fun String.file(): File {
        val file = kotlin.io.path.createTempFile().toFile()
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (!file.exists()) file.createNewFile()

        file.writeText(this)

        return file
    }

    private fun File.copy(path: String, name: String): File {
        val file = File(path, name)
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (!file.exists()) file.createNewFile()
        this.copyTo(file, true)
        return file
    }
}