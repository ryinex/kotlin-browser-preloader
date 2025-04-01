package files

internal fun preloaderHtml(imageName: String) = """
    <div class="loader-container" style="width: 100%;">
        <div class="loader"></div>
        <h2 id="progress" style="font-family: Tajawal; text-align: center;"></h2>
        <h2 style="font-family: Tajawal; text-align: center;">Loading ...</h2>
        <img src="./preloader/$imageName" style="height: 80px;">
    </div>
""".trimIndent()