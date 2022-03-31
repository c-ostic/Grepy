# helloworld
Simple hello world application template

## Usage
```
~/helloworld$ java -jar target/helloworld-0.0.1-SNAPSHOT-jar-with-dependencies.jar -h
helloworld
Version: 0.0.1
usage: helloworld
 -h   Display this help text
 -v   Verbose mode
```

## Using the Template
1. Follow the [instructions](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template) to create a repository based on this GitHub template.
2. Change references to any urls, authors, and emails throughout the project. HINT: Don't forget the license.
3. Update the package name througout the project. HINT: Don't forget the folder structure.
4. Update the project name and references throughout the project including the pom.xml, this README, the Dockerfile, and source.
5. Update the version number appropriately.
6. Code away!

## Prerequisites
- [Docker](https://docker.com/) - Simple development environment setup
or
- A Java development environment like [OpenJDK](https://developers.redhat.com/products/openjdk/download) and [Apache Maven](https://maven.apache.org/)

## Building
[Apache Maven](https://maven.apache.org/) is the build tool of choice.  Simply issue `mvn install package` to install dependencies and build the jar.  
Use `mvn clean install compile verify test package site` to clean up existing targets, install dependencies, verify, run unit tests, build a jar, and create the documentation and reports.

## Running
Issue a `java -jar target/helloworld-<VERSION>-SNAPSHOT-jar-with-dependencies.jar` to run the target directly.

## Docker Dev Environment
1. Build the image by running `docker build -t helloworldtemplate .` in a terminal
2. Then run the container based on the image with `docker run -it --rm -v $PWD:/helloworld helloworldtemplate` on Linux or an absolute path in Windows like `docker run -it --rm -v /c/git/java-hello-world-template:/helloworld helloworldtemplate`
3. Use VSCode or your IDE of choice to edit your code locally.  HINT: With VSCode run `code .` from the terminal when in the folder to easily open it
4. Build and run the code as you would locally from the Docker container Bash prompt

## Testing
[JUnit](https://junit.org/junit5/) is used as the Test Framework. To execute unit tests run `mvn test`.  
To skip tests, add `-Dmaven.test.skip=true` as an argument to your Maven command.

## Contributing
- All code (classes, files, global variables, members, and methods) must be thoroughly documented using JavaDoc accepted format.  HINT: Use `mvn site`
- All existing unit tests must pass. HINT: Use `mvn test`
- New unit tests are encouraged!
- README, help text, makefile, and other artifacts must be up to date.
- Program version number should be incremented.  HINT: In the *pom.xml* and *global variables*
- Use proper git ettiquete with frequent commits and [good commit messages](https://cbea.ms/git-commit/).
- All code must be formatted properly and verified with *checkstyle*.  HINT: Use `mvn verify`
- Binary files and other metadata shall not be committed. HINT: Update the .gitignore, if necessary
- No warnings or errors shall be produced by Maven with `mvn verify test site compile` options.
