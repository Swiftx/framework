# Swiftx Framework

面向 Java 21 和 Spring Boot 4.1 的公共库，采用 MIT 许可证。从 Maven 或 Gradle 按需引入各模块，所有制品的 groupId 均为 `top.swiftx.framework`。

| 模块 | artifactId | 能力 |
| --- | --- | --- |
| BOM | `dependencies` | 统一管理 REST、Problem 和 Storage 的版本 |
| REST | `rest` | 分页、ETag 条件请求、标准异常、MVC 和 WebFlux 的 Problem Details 响应 |
| Problem | `problem` | 从 Markdown 生成问题类型文档，并为错误响应设置文档 URI |
| Storage | `storage` | 存储桶注解、文件和目录接口，供实现方扩展 |
| ORM | `orm` | 预留持久化模块，尚未发布，未纳入 BOM |

## 使用 BOM 管理版本

在构建文件中导入一次 `top.swiftx.framework:dependencies`，再声明要使用的模块。下面以 REST 和 Problem 为例，两个模块的版本都由 BOM 提供。

### Gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation platform('top.swiftx.framework:dependencies:0.1.0')
    implementation 'top.swiftx.framework:rest'
    implementation 'top.swiftx.framework:problem'
}
```

### Maven

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>top.swiftx.framework</groupId>
            <artifactId>dependencies</artifactId>
            <version>0.1.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>top.swiftx.framework</groupId>
        <artifactId>rest</artifactId>
    </dependency>
    <dependency>
        <groupId>top.swiftx.framework</groupId>
        <artifactId>problem</artifactId>
    </dependency>
</dependencies>
```

Web 应用自行选择 Spring MVC 或 WebFlux starter。Framework 依据应用类型注册对应的异常处理器。把问题说明放入应用的 `src/main/resources/problems/<slug>.md`，启动后即可访问 `/problems/<slug>`。可通过 `framework.rest.enabled` 和 `framework.problem.enabled` 分别控制两组自动配置。

## 本地试用

使用 JDK 21 构建并发布开发快照到本机 Maven 仓库。

```shell
./gradlew clean build publishToMavenLocal
```

将上例中的 BOM 版本换成 `0.1.0-SNAPSHOT`。使用 Gradle 时，在 `repositories` 中加入 `mavenLocal()`。

## 发布到 Maven Central

发布账号需要拥有 `top.swiftx.framework` 命名空间的发布权限。将 Central Portal 用户令牌的用户名、密码和已分发公钥对应的签名私钥放入发布环境。Gradle 使用以下环境变量：

| 环境变量 | 内容 |
| --- | --- |
| `ORG_GRADLE_PROJECT_mavenCentralUsername` | Central Portal 用户令牌的用户名 |
| `ORG_GRADLE_PROJECT_mavenCentralPassword` | Central Portal 用户令牌的密码 |
| `ORG_GRADLE_PROJECT_signingInMemoryKey` | ASCII 装甲格式的 OpenPGP 私钥 |
| `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword` | 私钥口令，如已设置 |

为 BOM 和三个库选定一个新的正式版本，构建后上传到 Central Portal 待审。下例使用 `0.1.1`。

```shell
./gradlew clean build
./gradlew publishToMavenCentral -PframeworkVersion=0.1.1
```

在 Portal 中核对制品、来源包、Javadoc、签名和校验结果，确认后点击 Publish。正式版本可从 `mavenCentral()` 按同一坐标获取。[Central Portal 发布说明](https://central.sonatype.org/publish/publish-portal-gradle/)

使用旧坐标 `framework-rest`、`framework-problem` 或 `framework-storage` 的项目，可分别换成 `rest`、`problem` 或 `storage`，Java 包名保持一致。

早期 Java 17 系列使用 `rest-core` 和 `spring-boot-starter-rest` 坐标。本文的 Java 21 系列使用上表中的新坐标。
