package net.fze.lib.jdbc

enum class Pools(val value: Int) {
    HikariCP(0),
    Agroal(1),
    C3p0(2)
}