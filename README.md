# Swiftx Framework

面向 Java 21 和 Spring Boot 4.1 的公共库，采用 MIT 许可证。从 Maven 或 Gradle 按需引入各模块，所有制品的 groupId 均为 `top.swiftx.framework`。

| 模块 | artifactId | 能力 |
| --- | --- | --- |
| REST | `framework-rest` | 分页、ETag 条件请求、标准异常、MVC 和 WebFlux 的 Problem Details 响应 |
| Problem | `framework-problem` | 从 Markdown 生成问题类型文档，并为错误响应设置文档 URI |
| Storage | `framework-storage` | 存储桶注解、文件和目录接口，供实现方扩展 |
| ORM | — | 预留持久化模块，已有实现后再发布 |

## 本地试用

在 JDK 21 环境下运行：

```shell
./gradlew clean build publishToMavenLocal
```

默认开发版本为 `0.1.0-SNAPSHOT`。在消费项目中添加本地 Maven 仓库，并选择需要的依赖：

```groovy
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation 'top.swiftx.framework:framework-rest:0.1.0-SNAPSHOT'
    implementation 'top.swiftx.framework:framework-problem:0.1.0-SNAPSHOT'
}
```

Maven 项目可在 `pom.xml` 中引用同一制品：

```xml
<dependency>
    <groupId>top.swiftx.framework</groupId>
    <artifactId>framework-rest</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Web 应用自行选择 Spring MVC 或 WebFlux starter。Framework 依据应用类型注册对应的异常处理器。把问题说明放入应用的 `src/main/resources/problems/<slug>.md`，启动后即可访问 `/problems/<slug>`。可通过 `framework.rest.enabled` 和 `framework.problem.enabled` 分别控制两组自动配置。

## 发布到 Maven Central

发布账号需要拥有 `top.swiftx.framework` 命名空间的发布权限。将 Central Portal 用户令牌的用户名、密码和已分发公钥对应的签名私钥放入发布环境。Gradle 使用以下环境变量：

| 环境变量 | 内容 |
| --- | --- |
| `ORG_GRADLE_PROJECT_mavenCentralUsername` | Central Portal 用户令牌的用户名 |
| `ORG_GRADLE_PROJECT_mavenCentralPassword` | Central Portal 用户令牌的密码 |
| `ORG_GRADLE_PROJECT_signingInMemoryKey` | ASCII 装甲格式的 OpenPGP 私钥 |
| `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword` | 私钥口令，如已设置 |

选定尚未发布的正式版本后，运行以下命令将三个制品上传到 Central Portal 待审。此处以 `0.1.0` 为例：

```shell
./gradlew clean build
./gradlew publishToMavenCentral -PframeworkVersion=0.1.0
```

在 Portal 中核对制品、来源包、Javadoc、签名和校验结果，确认后点击 Publish。正式版本可从 `mavenCentral()` 按同一坐标获取。[Central Portal 发布说明](https://central.sonatype.org/publish/publish-portal-gradle/)

早期 Java 17 系列使用 `rest-core` 和 `spring-boot-starter-rest` 坐标。本文的 Java 21 系列使用上表中的新坐标。
