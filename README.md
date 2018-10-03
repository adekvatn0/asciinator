# What is Asciinator?

Asciinator is a simple Kotlin library to process images to ASCII style

# Quickstart

To begin using Asciinator in your program you need to add this to pom.xml
```xml
<dependencies>
        <dependency>
            <groupId>io.github.ambershogun</groupId>
            <artifactId>asciinator</artifactId>
            <version>1.2</version>
        </dependency>
</dependencies>
```

# Sample

```kotlin
Asciinator.asciinate(
    inputFile = File("/file/system/path/input.jpg"),
    width = 100,
    bgndColor = Color.BLACK,
    fontColor = Color.WHITE,
    outputFile = File("/file/system/path/output.png")
)
```