package files

internal val preloaderCss = """
    
    @import url('https://fonts.googleapis.com/css?family=Tajawal');

    .loader-container {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        flex-flow: column;
        align-items: center;
        grid-gap: 5px;
        margin: auto;
        max-width: 100%;
        max-height: 100%;
        text-align: center;
    }

    .h2 {
        margin: 0;
    }

    .loader {
        border: 16px solid #111318;
        border-top: 16px solid #B4CBFF;
        border-radius: 50%;
        width: 120px;
        height: 120px;
        animation: spin 2s linear infinite;
    }

    @keyframes spin {
        0% {
            transform: rotate(0deg);
        }

        100% {
            transform: rotate(360deg);
        }
    }

    @media (prefers-color-scheme: light) {
        body {
            background-color: #F9F9FF;
        }
        .loader-container {
            background-color: #F9F9FF;
            color: #00316F;
        }
        .loader {
            border: 16px solid #F9F9FF;
            border-top: 16px solid #00316F;
        }
    }

    @media (prefers-color-scheme: dark) {
        body {
            background-color: #111318;
        }
        .loader-container {
            background-color: #111318;
            color: #B4CBFF;
        }
        .loader {
            border: 16px solid #111318;
            border-top: 16px solid #B4CBFF;
        }
    }
""".trimIndent()