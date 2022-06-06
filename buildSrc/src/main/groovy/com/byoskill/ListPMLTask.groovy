package com.byoskill

import org.gradle.api.DefaultTask
import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.file.*

class ListPMLTask extends DefaultTask {

    @IgnoreEmptyDirectories
    @InputDirectory
    File sourceFolder
    
    ListPMLTask() {
        
    }

    @TaskAction()
    def listFiles() {
        // https://docs.gradle.org/current/userguide/more_about_tasks.html

        /**
         Create a file tree with a base directory
         ConfigurableFileTree tree = fileTree(dir: 'src/main')dfghjklà
         **/
        // Add include and exclude patterns to the tree
        // tree.include '**/*.java'
        // tree.exclude '**/Abstract*'

        // Create a tree using closure
        // tree = fileTree('src') {
        // include '**/*.java'
        // }
        println '------------------------------------------'
        def srcDir = project.file(sourceFolder)
        def pmlList = project.fileTree(dir: srcDir, include: '**/*.puml')
        def plantumlList = project.fileTree(dir: srcDir, include: '**/*.plantuml')
        def fullList = pmlList + plantumlList
        println "Found ${fullList.size()} diagrams"
        fullList.each { println it }

    }

}
