## Cross Type Pure Unable to Set Key

This is an example project where its not possible to set a key before an error is thrown
when using `sys.error`. In this case, we are trying to set `kioTeamName` (see [here](https://github.com/zalando-incubator/sbt-stups/blob/master/src/main/scala/sbtstups/SbtStupsPlugin.scala#L43-L44)
for more info). `sys.error` is used in the initial value of `kioTeamName` to make sure that the value is set (since there is
no sensible default).

The plugin that is causing the error is `sbt-stups` and it can be seen in `project/plugins.sbt`