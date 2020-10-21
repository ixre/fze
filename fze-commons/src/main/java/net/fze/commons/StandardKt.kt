package net.fze.commons

import com.google.gson.internal.JavaVersion

/** 标准类 */
private class StandardKt {
    companion object {
        private var devFlag: Int = -1
        private val java9OrLater = detectIsJava9OrLater()

        private fun detectIsJava9OrLater(): Boolean {
            val version = System.getProperty("java.version")
            if (Regex("^\\d+$").matches(version)) {
                return version.toInt() >= 9
            }
            return false
        }

        @JvmStatic
        fun isJava9OrLater():Boolean = java9OrLater

        @JvmStatic
        fun classInJar(c: Class<*>): Boolean {
            //var className= Thread.currentThread().stackTrace[1].className;
            // val c = Class.forName(className)
            //var c = Typed::class.java
            var pkg = if(isJava9OrLater())c.packageName else c.`package`.name
            val resName = pkg.replace(".", "/")
            val pkgPath = c.classLoader.getResource(resName)
            return (pkgPath?.path ?: "").indexOf(".jar!") == -1
        }

        /** 解析环境,如果是生产环境返回true,反之返回false */
        @JvmStatic
        fun resolveEnvironment(main: Class<*>):Boolean {
            devFlag = if (classInJar(main)) 1 else 0
            if (dev()) {
                // 在IDEA下开发时设置项目真实的工作空间
                var workspace = System.getProperty("user.dir")
                // Windows下以"\"分隔
                var i = workspace.indexOfAny(arrayListOf("/build", "/target","\\build", "\\target"))
                if (i != -1) {
                    System.setProperty("user.dir", workspace.substring(0, i))
                }
                return false
            }
            return true
        }
        @JvmStatic
        fun dev(): Boolean {
            if (devFlag == -1) throw Exception("should call method Standard.resolveEnvironment first")
            return devFlag == 1
        }
    }
}
