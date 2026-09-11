package com.hackclub.molten.auth

import io.ktor.client.engine.HttpClientEngineFactory

expect fun platformHttpClientEngine(): HttpClientEngineFactory<*>
