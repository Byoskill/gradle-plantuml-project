package com.byoskill

import org.gradle.api.DefaultTask
import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.file.*


class CreateOutputFolderTask extends DefaultTask {

    @Input
    String outputFolderPath
    
    @OutputDirectory
    File getOutputFolder() {
        return project.file(outputFolderPath)
    }

    @TaskAction
    buildOutputFolder() {
        def folderToBuild = new File(outputFolderPath)
        if (!folderToBuild.exists()) {
            new File(outputFolderPath).mkdirs()
            println "Plantuml generation folder created in ${outputFolder}"
        }

    }

}