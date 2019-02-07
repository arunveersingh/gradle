import org.apache.commons.codec.binary.Base64

group = "com.arunveersingh.kotlin"
version = "1.0-SNAPSHOT"


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        "classpath"(group = "commons-codec", name = "commons-codec", version = "1.5")
    }
}

tasks.register("taskA"){
    dependsOn("taskB")
    doLast{
        println("taskK")
    }
}
tasks.register("taskB"){
    doLast{
        println("taskB")
    }
}

// Dynamic tasks creation
repeat(4){
    counter ->
        tasks.register("task$counter") {
            doLast {
                println("I am task number $counter")
            }
        }
    }

// Enhancing existing task
tasks.named("task2"){
    dependsOn("task1", "task3")
}

// Accessing a task via API
val hello by tasks.registering{
    doLast{
        println("In last!")
    }
}

hello {
    doFirst{
        println("At First!")
    }
}

hello {
    doLast{
        println("In last!!")
    }
}

hello {
    doFirst{
        println("At First!!")
    }
}

tasks.register("welcome"){
    extra["property_a"] = "property a"
    doLast{
        println("Welcome task!")
    }
}

tasks.register("printProperties"){
    println(tasks["welcome"].extra["property_a"])
}

// Ant tasks execution with groovy
tasks.register("loadFile") {
    doLast {
        val files = file("./").listFiles().sorted()
        files.forEach { file ->

            println(" *** ${file.name} ***")

            if (file.isFile) {
                ant.withGroovyBuilder {
                    "loadfile"("srcFile" to file, "property" to file.name)
                }
                println(" *** ${file.name} ***")
                println("${ant.properties[file.name]}")
            }
        }
    }
}

fun listOfStrings() : List<String>{
    return arrayListOf("a", "b")
}

tasks.register("printList"){
    doLast{
        println(listOfStrings())
    }
}

defaultTasks("defA", "defB")

tasks.register("defA"){
    println("default task defA")
}
tasks.register("defB"){
    println("default task defB")
    dependsOn("welcome")
    doLast{
        println("in the doLast of task defB with ver=$version")
    }

gradle.taskGraph.whenReady {
    version = if(hasTask(":defA")) "1.0" else "-SNAPSHOT"
}

}

tasks.register("encode") {
    doLast {
        val encodedString = Base64().encode("hello world\n".toByteArray())
        println(String(encodedString))
    }
}