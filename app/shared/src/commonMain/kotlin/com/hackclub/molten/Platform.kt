package com.hackclub.molten

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform