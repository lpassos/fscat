# Index

  + [About](#about)
  + [Dependencies](#dependencies)
  + [Installation and usage](#installation-and-usage)
  + [Migration to GitHub](#migration-to-github)
  + [Contact](#contact)

---
# About

fscat is a tool for collecting feature constants from C-pre-processor-based
software families and scattering-related metrics. Different from other tools
(e.g., cppstats), fscat does not perform any code transformations.

---
# Dependencies

fscat depends on three programs, which must be in your system's executable path:

 + [src2srcml](http://www.sdml.info/): a C/C++ parser that preserves all code annotations (C-pre-processor
directives), outputting an XML-based AST (see note). 
 + [sline](https://bitbucket.org/lpassos/sline): a line-oriented C code cleaner
 + stripcomments: a simple bash script that removes all comments from a C file. The script is defined as

```bash
#------------------------
#!/bin/bash

if [[ "$#" != 1 ]] ; then
  echo "Usage: stripcomments input-file" > /dev/stderr
  exit
fi

gcc -fpreprocessed -dD -E -P "$1" 2> /dev/null
#------------------------
```

**IMPORTANT NOTE:** fscat has been tested with trunk version 13990 of src2srcml, as given in the tool's SVN repo. 

---
# Installation and usage 

Once src2srcml, sline, and stripcomments are set up (see previous section), 
download the fscat jar file from our Downloads page 
(https://bitbucket.org/lpassos/fscat/downloads).

Then, call it as:

`java -jar fscat_v0.2beta.jar`

in the same directory as the jar file was save. For your convenience, you can copy the 
following script to your system path:

```bash
#!/bin/bash

FSCAT_JAR="<directory where fscat.jar is placed>/fscat_v0.2beta.jar"

java -jar "$FSCAT_JAR" $@
```

and name it as `fscat`. Once you make the script executable (chmod +x), you can simply 
call fscat from any place in your system ;)

---
# Migration to GitHub

Our existing BitBucket repository is slowly being migrated to GitHub, 
but as long as this process is not fully completed, the BitBucket
copy remains the official one. Our BitBucket repository can be found 
[here](https://bitbucket.org/lpassos/fscat/overview).

---
# Contact

 + Leonardo Passos (lpassos@gsd.uwaterloo.ca)
 + Rodrigo Queiroz (rodrigoqz@gmail.com)
