To generate the [RxRapidApi](https://github.com/RxRapidApi/RxRapidApi) interface for a given 
[Rapid Api](https://www.rapidapi.com/) service you need to first 
locate the ```metadata.json``` file it its Github repo.  For example, *Github* can be found at
https://github.com/RapidSoftwareSolutions/Marketplace-Github-Package and its ```metadata.json```
file is at https://raw.githubusercontent.com/RapidSoftwareSolutions/Marketplace-Github-Package/master/metadata.json

Next you need to edit the code generation ```build.gradle``` file to enter the URL 

```groovy
task run (type: JavaExec, dependsOn: classes){
    args("https://raw.githubusercontent.com/RapidSoftwareSolutions/Marketplace-Github-Package/master/metadata.json")
    description = "Code Generation"
    main = "com.gatebuzz.rapidapi.rx.generator.RxRapidApiCodeGenerator"
    classpath = sourceSets.main.runtimeClasspath
}
```

then on the command line, execute the gradle "run" command

```
$ ./gradlew run
:compileJava
:processResources UP-TO-DATE
:classes
:run
Metadata downloaded.
Github.java created.

BUILD SUCCESSFUL
```

Finally, check out the file that was generated.
