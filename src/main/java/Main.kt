package ru.redshogun.ascii

import java.awt.Color
import java.io.File

fun main(args: Array<String>) {
    if (args.size < 5) {
        println("Usage: java -jar asciinator.jar <inputFile> <width> <backgroundColor> <fontColor> <outputFile>")
        System.exit(2)
    } else {
        try {
            Asciinator.asciinate(
                    inputFile = File(args[0]),
                    width = args[1].toInt(),
                    bgndColor = Color(args[2].toInt()),
                    fontColor = Color(args[3].toInt()),
                    outputFile = File(args[4])
            )
            println("Image successfully asciinated to ${args[4]}")
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace(System.err)
            System.exit(1)
        }
    }
}