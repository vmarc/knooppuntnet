#Developers

The information here is intended for people who want to contribute to the 
project. 

## How to setup the development environment to contribute to the web application?


The project uses the **sbt** build tool to support the development. The first step in
to install **sbt**. Platform specific instructions can be found here. 

```
https://www.scala-sbt.org/1.x/docs/Setup.html
```

Make sure that **npm** installed

```
????
```

Make sure that **git** is installed on your computer. If not, example installation instructions
can be found here:

```
https://git-scm.com/book/en/v2/Getting-Started-Installing-Git
```

Then clone the source code from github:

```
git clone https://github.com/vmarc/knooppuntnet.git

```

Switch to the **develop** branch to work with the most recent code:

```
git checkout develop
```

Xxxx

```
cd knooppuntnet/development/ui
npm update
```



Go to the development directory, and run sbt (the first time this will take a while,
as a lot of dependencies get downloaded):

```
cd knooppuntnet/development
sbt
```

Once **sbt** is showing its commandline prompt, type the following (again: the first time
this may take a while):

```
run
```

Now open your browser at the following address:

```
http://localhost:4200
```





## How to translate to different language?

