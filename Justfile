[parallel]
dev: server web

server:
    ./gradlew :server:run

web:
    ./gradlew :app:webApp:wasmJsBrowserDevelopmentRun