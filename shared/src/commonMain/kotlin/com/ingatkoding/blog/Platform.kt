package com.ingatkoding.blog

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform