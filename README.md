io.svc.scalac.hook
==================

## Prerequisites

### Scala

Scala version 2.10.1

### SBT

[paulp/sbt-extras](https://github.com/paulp/sbt-extras).

## Package

```
$ sbt package
```

## Usage

```
$ ~/programs/scala-2.10.1/bin/scala \
  -Xplugin:core/target/scala-2.10/scalac-hook_2.10-0.1-SNAPSHOT.jar,examples/target/scala-2.10/scalac-hook-examples_2.10-0.1-SNAPSHOT.jar \
  -P:hook:io.svc.scalac.hook.example.traverser.Println.PrintlnHook
```

