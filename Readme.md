A set of ready-made palettes for BeepBeep 3
===========================================

This repository contains projects producing various independent
extensions to the [BeepBeep 3](https://liflab.github.io/beepbeep-3)
event stream query engine. Each project is also independent from the others, and
can be built separately. All projects require `beepbeep-3.jar` in their
classpath (or alternately, must point to the `Core` source files from
BeepBeep's repository) in order to compile and run.

Available palettes
------------------

At the moment, the folder contains the following palettes:

- `Apache`: for parsing Apache log files
- `Concurrency`: for using multi-threading in processors
- `Diagnostics`: to help debugging processor chains
- `Fol`: for manipulation of first-order logic statements
- `Fsm`: for manipulation of extended finite-state machines
- `Gnuplot`: for drawing plots from event streams
- `Http`: send and receive events over a network
- `Json`: to read and write events in the JSON format
- `Ltl`: to express properties in Linear Temporal Logic
- `Mtnp`: front-end to the MTNP library to manipulate tables and plots
- `Provenance`: traceability in processor chains
- `Serialization`: serialize/deserialize objects with the
   [Azrael](https://github.com/sylvainhalle/Azrael) library
- `Sets`: to manipulate streams of sets
- `Signal`: basic signal processing functions (peak detection, etc.)
- `Tuples`: for manipulating tuples and using BeepBeep as a JDBC driver
- `WebSocket`: to read/write events from/to a web socket
- `Xml`: to read and write events in the JSON format

Depending on the projects, additional dependencies may need to be
downloaded and installed. Please refer to the `Readme.md` file of each
particular project (if any) for more information on compiling and
building these extensions.

Building the palettes
---------------------

### tl;dr

1. Build or download `beepbeep-3.jar` and put it in the root of this
   repository.
2. Run the script `build-all.sh` to build all palettes in succession.
3. If everything goes well, all the resulting jars will be created at the
   root of the repository. Move them around and enjoy.

### More details

First make sure you have the following installed:

- The Java Development Kit (JDK) to compile. BeepBeep was developed and
  tested on version 6 of the JDK, but it is probably safe to use either
  version 7 or 8.
- [Ant](http://ant.apache.org) to automate the compilation and build process

Each palette depends on BeepBeep's core library, which must be built
beforehand. Please refer to the build instructions for [BeepBeep's main
engine](https://github.com/liflab/beepbeep-3).
The resulting file, `beepbeep-3.jar`, must be placed at the root of this
repository prior to building any of the palettes.

The repository is separated into multiple *projects*. Each of these
projects has the same Ant build script that allows you to compile them
(see below).

If the project you want to compile has dependencies,
 you can automatically download any libraries missing from your
system by typing:

    ant download-deps

This will put the missing JAR files in the `deps` folder in the project's
root. These libraries should then be put somewhere in the classpath, such as
in Java's extension folder (don't leave them there, it won't work). You can
do that by typing (**with administrator rights**):

    ant install-deps

or by putting them manually in the extension folder. Type `ant init` and it
will print out what that folder is for your system.

Do **not** create subfolders there (i.e. put the archive directly in that
folder).

### Compiling

Compile the sources by simply typing:

    ant

This will produce a file called `xxx.jar` (depending on the palette you
are compiling) in the root folder.

In addition, the script generates in the `doc` folder the Javadoc
documentation for using BeepBeep. To show documentation in Eclipse,
right-click on the jar, click "Properties", then fill the Javadoc location.

### Testing

Some of these palettes can can test themselves by running:

    ant test

Unit tests are run with [jUnit](http://junit.org); a detailed report of
these tests in HTML format is availble in the folder `tests/junit`, which
is automatically created. Code coverage is also computed with
[JaCoCo](http://www.eclemma.org/jacoco/); a detailed report is available
in the folder `tests/coverage`.

### Creating a fat Zip file

Once all the palettes have been built, a (fat) zip file containing all the
generated JARs can be creating by typing:

    ant zip

in the root folder of this repository. The resulting file will be called
`beepbeep-3-palettes-vYYYYMMDD.zip`, where `YYYYMMDD` is the current date.
Some precompiled bundles are available in GitHub's *Releases* page, but they
may not contain the latest version of each palette. Use at your own risk!

Warning                                                          {#warning}
-------

The BeepBeep project is under heavy development. The repository may be
restructured, the API may change, and so on. This is R&D!

About the author                                                   {#about}
----------------

BeepBeep 3 was written by [Sylvain Hallé](http://leduotang.ca/sylvain),
associate professor at Université du Québec à Chicoutimi, Canada. Part of
this work has been funded by the Canada Research Chair in Software
Specification, Testing and Verification and the
[Natural Sciences and Engineering Research Council
of Canada](http://nserc-crsng.gc.ca).
