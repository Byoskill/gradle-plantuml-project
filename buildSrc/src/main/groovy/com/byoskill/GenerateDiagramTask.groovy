package com.byoskill

import groovy.transform.ToString
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

// https://discuss.gradle.org/t/how-to-pass-data-from-one-task-to-another/3980
//@CacheableTask
class GenerateDiagramTask extends DefaultTask {
    @PathSensitive(PathSensitivity.ABSOLUTE)
    @InputDirectory
    File sourceFolder

    @PathSensitive(PathSensitivity.ABSOLUTE)
    @InputDirectory
    File buildFolder

    @OutputFiles
    List<File> outdatedFiles = new ArrayList<>()

    @ToString
    static class GenerationAction {
        File inputFile
        File outputFile

        boolean isUpToDate() {
            return outputFile.exists()
        }

        File generate() {
            println "Generation of $outputFile"

            def args = ["java",
                        "-jar", "lib/plantuml-1.2022.5.jar",
                        "-stdrpt:1",
                        inputFile.absolutePath,
                        "-o", outputFile.parentFile.absolutePath,
                        "tpng"]
            println args
            def proc = args.execute()
            proc.in.eachLine { line -> println line }
            proc.err.eachLine { line -> println 'ERROR: ' + line }
            proc.waitFor()
            return outputFile
        }
    }

    @TaskAction
    findPMLToGenerate() {
        List<GenerationAction> diagramsToBeGenerated = []
        def pmlList = project.fileTree(dir: sourceFolder, include: '**/*.puml')
        def plantumlList = project.fileTree(dir: sourceFolder, include: '**/*.plantuml')

        // Identify file to be generated
        FileTree fullList = pmlList + plantumlList
        fullList.each { it ->
            {
                def expectedFile = new File(buildFolder, it.name.replaceFirst(~/\.[^\.]+$/, '') + ".png")
                println "Checking ${it} to be generated in ${expectedFile}"
                def action = new GenerationAction([inputFile: it, outputFile: expectedFile])
                if (!action.isUpToDate()) {
                    diagramsToBeGenerated.add(action)
                }
            }
        }
        // Generate the missing files and add them as output files
        diagramsToBeGenerated.each { GenerationAction it -> outdatedFiles.add(it.generate()) }
    }

}