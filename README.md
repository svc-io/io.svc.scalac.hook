scalac hook
===========
Scala compiler hook to simplify working with scala compiler plugins.

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
$ scala \
  -Xplugin:core/target/scala-2.10/scalac-hook_2.10-0.1-SNAPSHOT.jar,examples/target/scala-2.10/scalac-hook-examples_2.10-0.1-SNAPSHOT.jar \
  -P:hook:io.svc.scalac.hook.example.traverser.Println.PrintlnHook
```

## Usage via SBT

```
$ sbt console
```


## Create your own hook

See the [Println example](https://github.com/svc-io/io.svc.scalac.hook/blob/master/examples/src/main/scala/io/svc/scalac/hook/example/traverser/Println.scala) 
for a dummy implementation that shows what you have to implement. Then take a fresh dive into the scala-compiler code.

## License

[BSD](http://www.opensource.org/licenses/bsd-license.php)

## Thanks!

Thanks to Paul Phillips and Eugene Burmako for their suggestions.
