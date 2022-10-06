添加仓库：

```
repositories {
  maven("http://git.tech.meizhuli.net:8082/repository/maven-public")
}
```

添加依赖：

```
implementation("net.fze.arch:fze-commons:0.1.10")
```
maven中引用
```
<dependency>
    <groupId>net.fze</groupId>
    <artifactId>fze-commons</artifactId>
    <version>0.3.9</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/fze-commons-0.3.9.jar</systemPath>
</dependency>
```
