# Ammonite

## `import $file` clashing with `save/load session`

```
vu@Vu-Gazelle:~/github/misc/Scala$ cat tmp.sc
object Ob {
  val c = 0
}


vu@Vu-Gazelle:~/github/misc/Scala$ amm
Loading...
Welcome to the Ammonite Repl 1.0.2
(Scala 2.12.3 Java 1.8.0_144)
If you like Ammonite, please support our development at www.patreon.com/lihaoyi

@ repl.sess.save() /* save session */

@ import $file.tmp /* import file `tmp.sc` to unsaved session */
Compiling /home/vu/github/misc/Scala/tmp.sc
import $file.$

@ tmp.Ob.c
res2: Int = 0

@ repl.sess.load() /* load saved session */
res3: runtime.SessionChanged = SessionChanged(Set('res0, 'tmp, 'res2), Set(), Set(), Set())

@ tmp.Ob.c /* should and will be `not found` */
cmd4.sc:1: not found: value tmp
val res4 = tmp.Ob.c /* will be `not found` */
           ^
Compilation Failed

@ import $file.tmp /* import file `tmp.sc` to saved session */
import $file.$

@ tmp.Ob.c /* should be `0`, but an exception will be thrown */
java.lang.NoClassDefFoundError: ammonite/$file/tmp$Ob$
  ammonite.$sess.cmd5$.<init>(cmd5.sc:1)
  ammonite.$sess.cmd5$.<clinit>(cmd5.sc)
java.lang.ClassNotFoundException: ammonite.$file.tmp$Ob$
  java.net.URLClassLoader.findClass(URLClassLoader.java:381)
  ammonite.runtime.SpecialClassLoader.findClass(ClassLoaders.scala:198)
  java.lang.ClassLoader.loadClass(ClassLoader.java:424)
  java.lang.ClassLoader.loadClass(ClassLoader.java:357)
  ammonite.$sess.cmd5$.<init>(cmd5.sc:1)
  ammonite.$sess.cmd5$.<clinit>(cmd5.sc)
@
```
