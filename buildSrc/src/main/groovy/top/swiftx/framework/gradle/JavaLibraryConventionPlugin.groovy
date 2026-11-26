package top.swiftx.framework.gradle

import groovy.transform.CompileStatic
import io.github.malczuuu.nullmarked.dsl.NullMarkedExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.CopySpec
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

@CompileStatic
class JavaLibraryConventionPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply('java-library')
        project.pluginManager.apply('jacoco')
        project.pluginManager.apply('io.github.malczuuu.nullmarked')

        def java = project.extensions.getByType(JavaPluginExtension)
        java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        project.extensions.getByType(NullMarkedExtension).sourceSet('test')
        project.extensions.getByType(JacocoPluginExtension).toolVersion = '0.8.15'

        configureDependencies(project)
        configureTasks(project)
    }

    private static void configureDependencies(Project project) {
        def libs = project.extensions.getByType(VersionCatalogsExtension).named('libs')
        def dependencies = project.dependencies
        def springBootBom = dependencies.platform(libs.findLibrary('spring-boot-bom').get())

        dependencies.addProvider('api', springBootBom)
        dependencies.add('api', 'org.springframework.boot:spring-boot-autoconfigure')
        dependencies.add('api', 'org.jspecify:jspecify')
        dependencies.add('implementation', 'org.slf4j:slf4j-api')
        dependencies.add('compileOnly', 'org.projectlombok:lombok')
        dependencies.addProvider('annotationProcessor', springBootBom)
        dependencies.add('annotationProcessor', 'org.projectlombok:lombok')
        dependencies.add('annotationProcessor', 'org.springframework.boot:spring-boot-configuration-processor')
        dependencies.add('testImplementation', 'org.springframework.boot:spring-boot-starter-test')
        dependencies.add('testRuntimeOnly', 'org.junit.platform:junit-platform-launcher')
    }

    private static void configureTasks(Project project) {
        def tasks = project.tasks
        tasks.withType(JavaCompile).configureEach { JavaCompile task ->
            task.options.encoding = 'UTF-8'
            task.options.compilerArgs.add('-parameters')
        }
        tasks.withType(Javadoc).configureEach { Javadoc task ->
            def options = (StandardJavadocDocletOptions) task.options
            options.encoding = 'UTF-8'
            options.addBooleanOption('Xdoclint:all,-missing', true)
        }

        def report = tasks.named('jacocoTestReport', JacocoReport)
        report.configure { JacocoReport task ->
            task.reports.xml.required.set(true)
        }
        tasks.named('test', Test).configure { Test task ->
            task.useJUnitPlatform()
            task.finalizedBy(report)
        }

        def license = project.rootProject.file('LICENSE')
        tasks.withType(Jar).configureEach { Jar jar ->
            jar.metaInf { CopySpec spec -> spec.from(license) }
        }
    }
}
