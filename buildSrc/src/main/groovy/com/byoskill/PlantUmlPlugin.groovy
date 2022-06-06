package com.byoskill

import com.byoskill.CreateOutputFolderTask
import com.byoskill.GenerateDiagramTask
import com.byoskill.ListPMLTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property

abstract class PlantUmlPluginExtension {
    abstract Property<File> getSourceFolder()

    abstract Property<File> getOutputFolder()

    PlantUmlPluginExtension() {
        sourceFolder.convention(new File("src/plantuml"))
        outputFolder.convention(new File("build/plantuml"))
    }
}


class PlantUmlPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Add the 'greeting' extension object
        def extension = project.extensions.create('plantuml', PlantUmlPluginExtension)

        def createOutputTask = project.tasks.register("createOutputFolder", CreateOutputFolderTask) {
            group = "PlantUML"
            description = "Creates the output folder if necessary"
            outputFolderPath = project.plantuml.outputFolder.get().absolutePath
        }


        // Implémenter avec un listFiles
        // Puis avec un files()
        // Puis aprŝ avec un fileTree
        def listPML = project.tasks.register('listPML', ListPMLTask) {
            group = 'PlantUML'
            description = 'List the available diagrams'
            sourceFolder = project.plantuml.sourceFolder.get()
        }

        // https://tomgregory.com/gradle-task-inputs-and-outputs/

        // Task extraction with https://docs.gradle.org/current/userguide/custom_tasks.html
        project.tasks.register("buildPlantUml", GenerateDiagramTask) {
            group = 'PlantUML'
            description = 'Build all PlantUML present diagrams'
            dependsOn createOutputTask, listPML
            sourceFolder = project.plantuml.sourceFolder.get()
            buildFolder = createOutputTask.get().outputFolder
        }

        project.task('plantUmlTest') {
            group = 'PlantUML'
            description = 'Test the PlantUML installation'
        }

        project.task('plantUmlGui') {
            group = 'PlantUML'
            description = 'Display the PlantUML Gui'
        }

        project.task('displayImages') {
            group = 'PlantUML'
            description = 'Display the PlantUML generated images'

        }
    }
}
