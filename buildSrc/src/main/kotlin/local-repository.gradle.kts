/*
 * Plugin that configures publication of all modules in a project to a local repository folder.
 * This facilitates integration testing when artifacts from the current build are needed.
 */

package buildsrc.convention

import java.net.URI

abstract class LocalRepositoryExtension {

    /**
     * Directory in which the local repository is maintained.
     */
    @get:Input
    abstract val dir: DirectoryProperty

    /**
     * Name of the local repository. This name is used in publication tasks.
     */
    @get:Input
    abstract val repositoryName: Property<String>

    val absoluteUri: URI
        get() = dir.get().asFile.absoluteFile.toURI()

    /**
     * Finds the publication task for publishing artifacts to the local repository within the provided [project].
     */
    fun findPublicationTask(project: Project): Task? = project.pluginManager.hasPlugin("maven-publish")
        .takeIf { it }
        ?.let {
            val capitalizedName = repositoryName.get().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
            project.tasks.getByName("publishAllPublicationsTo${capitalizedName}Repository")
        }
}

val extension = extensions.create<LocalRepositoryExtension>("localRepository").apply {
    dir.convention(rootProject.layout.buildDirectory.dir("repository").get()).finalizeValueOnRead()
    repositoryName.convention("local").finalizeValueOnRead()
}

allprojects {
    // Add publication target to the local repository in all (sub)projects that have publications configured
    pluginManager.withPlugin("maven-publish") {
        extensions.configure<PublishingExtension> {
            repositories.maven {
                name = extension.repositoryName.get()
                url = extension.absoluteUri
            }
        }
    }

    // Make the configuration available in all (sub)projects
    if (extensions.findByType<LocalRepositoryExtension>() == null) {
        extensions.add("localRepository", extension)
    }
}
