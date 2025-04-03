# Browser Preloader Plugin

A Gradle plugin that adds a loading screen to your kotlin/wasm web application while the application is being loaded.

## Features
- Customizable loading screen with SVG logo support
- Progress tracking for resource loading
- CSS customization options
- HTML template customization

## Playground
You can try out the plugin in sample project: [Sample](https://ryinex.github.io/kotlin-browser-preloader)           
You can try out the plugin in actual project: [CSV Viewer](https://csvviewer.ryinex.com)

## Installation
place this plugin in your build.gradle.kts:

```kotlin
plugins {
    id("com.ryinex.kotlin.browser.preloader") version "<latest-version>"
}

preloader {
    jsModuleName.set("your-module-name")
    logo.set(project.file("path/to/your/logo.svg"))
}
```

## Summary

The plugin will:
- Scan your project for resources, wasm and js files
- Generate a preloader configuration
- Inject required preloader scripts and styles into your index.html
- Add a loading screen that displays progress while your application loads

## Configuration Options

- `jsModuleName`: Your application's main JS module name
- `logo`: SVG logo to display during loading (required with default template)
- `css`: Custom CSS file for styling the preloader
- `html`: Custom HTML template for the preloader
- `lengthHeader`: Custom header name for content length tracking
- `distributionPath`: Custom distribution directory for output files

```kotlin
preloader {
    // Required
    jsModuleName.set("your-module-name")
    
    // Required if using default html template
    logo.set(project.file("path/to/your/logo.svg"))
    
    // Optional - custom styling
    css.set(project.file("path/to/custom/preloader.css"))
    
    // Optional - custom HTML template
    html.set(project.file("path/to/custom/preloader.html"))
    
    // Optional - distribution directory in case if you are changing the default output directory for the output files
    distributionPath.set("<path>")
    
    // Optional - custom header for actual content length in case of compression (e.g. gzip or br) to see actual loading progress (e.g. 59%)
    // In cases of compression, the `Content-Length` header may not be available or it doesn't represent the actual content length
    // So you need to configure your server to add a custom header with the actual content length
    lengthHeader.set("x-decompressed-content-length")
}
```


## Custom HTML Template Requirements

When using a custom HTML template, ensure your template includes:

1. Container with id `loader-container` that includes everything inside it
2. A text element with id `progress` for loading progress display

Example structure:
```html
<div class="loader-container" style="width: 100%;">
    <div class="loader"></div>
    <h2 id="progress" style="font-family: Tajawal; text-align: center;"></h2>
    <h2 style="font-family: Tajawal; text-align: center;">Loading ...</h2>
    <img src="./preloader/$imageName" style="height: 80px;">
</div>
```

## Development

Enable logging for debugging:
```kotlin
preloader {
    logEnabled.set(true)
}
```

## License

Apache License 2.0 - See [LICENSE](LICENSE) for details.