import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get

class PreloaderPlugin() : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val task = tasks.register("browserPreloader", PreloaderTask::class.java)
        val extension = extensions.create("preloader", PreloaderPluginExtensions::class.java)
        val defaultBuild = "${target.layout.buildDirectory.asFile.get().path}/dist/wasmJs/productionExecutable"
        val path = extension.distributionPath.getOrElse(defaultBuild).removeSuffix("/")

        task.configure {
            val file = file("$path/preloader")
            outputDistributionDir.set(file.absolutePath)

            jsModuleName.set(extension.jsModuleName)
            css.set(extension.css)
            html.set(extension.html)
            logo.set(extension.logo)
            lengthHeader.set(extension.lengthHeader)

            logEnabled = extension.logEnabled.getOrElse(false)
        }

        afterEvaluate {
            checks(extension)

            target.tasks["wasmJsBrowserDistribution"].finalizedBy(task)
        }
    }

    private fun checks(extension: PreloaderPluginExtensions) {
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

        val moduleName = extension.jsModuleName.orNull ?: error(moduleNameError)
        val html = extension.html.orNull
        val logo = if (html == null) {
            extension.logo.getOrElse { error(logoErrorMessage) }
        } else {
            extension.logo.orNull
        }

        if (html == null && logo?.asFile?.exists() == false) error(logoErrorMessage)
    }
}