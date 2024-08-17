package com.kaaneneskpc.whellpicker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform