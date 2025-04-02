import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.io.path.absolutePathString

@Throws(IOException::class, InterruptedException::class)
internal fun script(path: String) {
    println("[LOG] Preloader script running")
    val scriptDir = kotlin.io.path.Path(path)

    val projectDir = scriptDir.parent
    val projectAbsolutePath = projectDir.absolutePathString()
    val resources = findFiles(projectAbsolutePath, projectDir.resolve("composeResources"), ".*")
    val wasms = findFiles(projectAbsolutePath, projectDir, ".*.wasm")
    val jsAll = findFiles(projectAbsolutePath, projectDir, ".*.*js")
    val js: MutableList<String> = ArrayList<String>()

    for (element in jsAll) {
        if (!element.endsWith("preloader/Preload.js") && !element.endsWith("preloader/preloader.js")) {
            js.add(element)
        }
    }

    val all: MutableList<String> = ArrayList<String>()
    all.addAll(resources)
    all.addAll(wasms)
    all.addAll(js)

    val listStrBuilder = StringBuilder("const list = [\n")
    for (element in all) {
        listStrBuilder.append("    \"")
            .append(element.replaceFirst("^\\./".toRegex(), ""))
            .append("\",\n")
    }
    listStrBuilder.append("]\n")
    val listStr = listStrBuilder.toString()

    val injectStart = "\n<!-- PRELOADER_INJECT_START -->\n"
    val injectEnd = "\n<!-- PRELOADER_INJECT_END -->\n"

    // Modify preloader.js
    val preloaderJsPath = projectDir.resolve("preloader/preloader.js")
    replaceInFile(preloaderJsPath, "const list = \\[(.|\n)*\\]", listStr)
//    replaceInFile(
//        preloaderJsPath,
//        "const moduleScript = \".*\"",
//        "const moduleScript = \"$wasmAppName\""
//    )

    // Modify index.html
    val indexPath = projectDir.resolve("index.html")
    removeBetweenMarkers(indexPath, injectStart, injectEnd)

    val preloaderCssLink =
        "$injectStart<link type=\"text/css\" rel=\"stylesheet\" href=\"preloader/preloader.css\">$injectEnd"
    insertBefore(indexPath, "</head>", preloaderCssLink)

    val preloaderHtml =
        injectStart + readFile(projectDir.resolve("preloader/preloader.html")) + injectEnd
    insertBefore(indexPath, "</body>", preloaderHtml)

    val preloadJsScript =
        "$injectStart<script src=\"preloader/Preload.js\"></script>$injectEnd"
    insertBefore(indexPath, "</html>", preloadJsScript)

    val preloaderScript =
        "$injectStart<script type=\"application/javascript\" src=\"preloader/preloader.js\"></script>$injectEnd"
    insertBefore(indexPath, "</html>", preloaderScript)
}

@Throws(IOException::class)
private fun findFiles(parent: String, directory: Path, pattern: String): List<String> {
    println("[LOG] Finding files in $directory, pattern: $pattern")
    directory.toFile().list()
    val result = directory
        .toFile()
        .allFileChildren()
        .map { it.absolutePath.replace("$parent/", "") }
        .filter { it.matches(pattern.toRegex()) }
    println("[LOG] Found files in $directory: ${result.size}")
    return result
}

private fun File.allFileChildren(): List<File> {
    println("[LOG] Finding files in $absolutePath")
    val list = mutableListOf<File>()

    for (file in this.listFiles() ?: emptyArray()) {
        if (file.isDirectory) {
            list.addAll(file.allFileChildren())
        } else {
            list.add(file)
        }
    }
    println("[LOG] Found files in $absolutePath: ${list.size}")

    return list
}

@Throws(IOException::class)
private fun replaceInFile(filePath: Path, regex: String, replacement: String?) {
    println("[LOG] Replacing in file $filePath, regex: $regex")
    val content = readFile(filePath)
    val pattern = Pattern.compile(regex, Pattern.DOTALL)
    val matcher = pattern.matcher(content)
    val newContent = matcher.replaceFirst(replacement)
    writeFile(filePath, newContent)
    println("[LOG] Replaced in file $filePath")
}

@Throws(IOException::class)
private fun removeBetweenMarkers(filePath: Path, startMarker: String, endMarker: String) {
    println("[LOG] Removing between markers in file $filePath,")
    val content = readFile(filePath)
    val pattern = Pattern.compile(
        Pattern.quote(startMarker) + "(.|\n)*?" + Pattern.quote(endMarker),
        Pattern.DOTALL
    )
    val matcher = pattern.matcher(content)
    val newContent = matcher.replaceAll("")
    writeFile(filePath, newContent)
    println("[LOG] Removed between markers in file $filePath")
}

@Throws(IOException::class)
private fun insertBefore(filePath: Path, target: String, insertion: String?) {
    println("[LOG] Inserting before marker in file $filePath")
    val content = readFile(filePath)
    val newContent = content.replace(target, insertion + target)
    writeFile(filePath, newContent)
    println("[LOG] Inserted before marker in file $filePath")
}

@Throws(IOException::class)
private fun readFile(filePath: Path): String {
    println("[LOG] Reading file $filePath")
    val content = String(Files.readAllBytes(filePath), StandardCharsets.UTF_8)
    println("[LOG] Read file $filePath")
    return content
}

@Throws(IOException::class)
private fun writeFile(filePath: Path, content: String) {
    println("[LOG] Writing file $filePath")
    Files.write(filePath, content.toByteArray(StandardCharsets.UTF_8))
    println("[LOG] Written file $filePath")
}