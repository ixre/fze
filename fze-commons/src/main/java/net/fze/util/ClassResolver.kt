package net.fze.util

import java.io.File
import java.io.IOException
import java.net.JarURLConnection
import java.net.URL
import java.net.URLDecoder
import java.util.*
import java.util.jar.JarFile


class ClassResolver {
    private val PATH_REGEXP = Regex("\\\\|/")

    internal constructor(){}
    /**
     * 获取包下所有的类型,[pkg]包名,[filter]筛选符合条件的类型，可以为空
     */
    fun getClasses(pkg: String, filter: Types.TCond<Class<*>>): Array<Class<*>> {
        try {
            var scope = pkg
            val loader = Thread.currentThread().contextClassLoader
            if (scope.isNullOrEmpty()) {
                val definedPackages = loader.definedPackages
                scope = definedPackages[definedPackages.size - 1].name
            }
            return walkPkg(loader, scope!!, filter).toTypedArray()
        } catch (ex: Throwable) {
            throw ex
        }
    }

    private fun walkPkg(loader: ClassLoader, pkg: String, filter: Types.TCond<Class<*>>?): List<Class<*>> {
        val packageDirName = pkg.replace(".", "/")
        //获取当前目录下面的所有的子目录的url
        val dirs = loader.getResources(packageDirName) ?: return arrayListOf()
        val arr = mutableListOf<Class<*>>()
        while (dirs.hasMoreElements()) {
            val url = dirs.nextElement()
            //获取包的物理路径
            if (url.protocol == "file") {
                var filePath: String = url.file
                try {
                    filePath = URLDecoder.decode(url.file, "UTF-8") ?: ""
                } catch (ex: Throwable) {
                    //ex.printStackTrace()
                }
                //println("---从文件中获取")
                arr.addAll(getFilePathClasses(loader, pkg, filePath, filePath.length, filter))
            }
            if (url.protocol == "jar") {
                //println("---从jar中获取")
                arr.addAll(getFilePathInJar(url, filter))
            }
        }
        return arr
    }

    fun getFilePathInJar(url: URL, filter: Types.TCond<Class<*>>?): ArrayList<Class<*>> {
        val classArray = ArrayList<Class<*>>()
        // 定义一个JarFile
        val jar: JarFile
        try {
            // 获取jar
            jar = (url.openConnection() as JarURLConnection).jarFile
            // 从此jar包 得到一个枚举类
            val entries = jar.entries()
            // 同样的进行循环迭代
            while (entries.hasMoreElements()) {
                // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                val entry = entries.nextElement()
                var name = entry.name
                // 如果是一个.class文件 而且不是目录
                if (!entry.isDirectory && name.endsWith(".class")) {
                    var classPath = name.replace('/', '.').substring(0, name.length - 6)
                    // 添加到classes
                    try {
                        val classes = Class.forName(classPath)
                        if (filter == null || filter.test(classes)) {
                            // println("----class=$classPath;")
                            classArray.add(classes)
                        }
                    } catch (ex: Throwable) {
                        //ex.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return classArray
    }

    /**
     * 从指定的包下面找到文件名
     */
    private fun getFilePathClasses(
            loader: ClassLoader, basePkg: String,
            filePath: String, pkgPathLen: Int,
            fn: Types.TCond<Class<*>>?): ArrayList<Class<*>> {
        val classArray = ArrayList<Class<*>>()
        val files = File(filePath).listFiles()
        for (fi in files) {
            if (fi.isDirectory) {
                classArray.addAll(getFilePathClasses(loader, basePkg, fi.path, pkgPathLen, fn))
            } else {
                var path = fi.path
                if (path.endsWith(".class")) {
                    path = path.replace(PATH_REGEXP, ".")
                    var part = path.substring(pkgPathLen, path.length - 6);
                    if (part[0] != '.') part = ".$part"; // windows下少"."
                    val classPath = basePkg + part
                    val classes = loader.loadClass(classPath)
                    if (fn == null || fn.test(classes)) {
                        //println("----class=$classPath;")
                        classArray.add(classes)
                    }
                }
            }
        }
        return classArray
    }
}