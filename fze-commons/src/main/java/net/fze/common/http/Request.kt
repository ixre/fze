package net.fze.common.http


enum class ContentTypes(var value: String) {
    NOT(""),
    FORM("application/x-www-form-urlencoded"),
    FILES("multipart/form-data"),
    JSON("application/json")
}

