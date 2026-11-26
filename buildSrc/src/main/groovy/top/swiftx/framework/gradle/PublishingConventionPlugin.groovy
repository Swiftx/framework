package top.swiftx.framework.gradle

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom

@CompileStatic
class PublishingConventionPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.pluginManager.apply('com.vanniktech.maven.publish')

        def publishing = project.extensions.getByType(MavenPublishBaseExtension)
        publishing.publishToMavenCentral()
        publishing.signAllPublications()
        publishing.pom { MavenPom pom ->
            pom.name.set(project.name)
            pom.description.set(project.providers.provider { project.description })
            pom.url.set('https://github.com/Swiftx/framework')
            pom.licenses { licenses ->
                licenses.license { license ->
                    license.name.set('MIT License')
                    license.url.set('https://opensource.org/license/mit')
                    license.distribution.set('repo')
                }
            }
            pom.developers { developers ->
                developers.developer { developer ->
                    developer.id.set('Swiftx')
                    developer.name.set('胡永强')
                    developer.url.set('https://github.com/Swiftx')
                }
            }
            pom.scm { scm ->
                scm.connection.set('scm:git:https://github.com/Swiftx/framework.git')
                scm.developerConnection.set('scm:git:ssh://git@github.com/Swiftx/framework.git')
                scm.url.set('https://github.com/Swiftx/framework')
            }
        }
    }
}
