import com.typesafe.sbt.rjs.Import.RjsKeys
import sbt.Keys._
import sbt.Project.projectToRef

lazy val clients = Seq(client)
lazy val scalaV  = "2.11.8"

lazy val scalatagsV = "0.6.0"
lazy val reactV     = "15.3.2"

lazy val logisticsCommonVersion = "0.1.0-SNAPSHOT"

scalaVersion in ThisBuild := scalaV
name in ThisBuild := """cross-type-pure-unable-to-set-key"""

scalafmtConfig in ThisBuild := Some(file(".scalafmt.conf"))

lazy val server = (project in file("jvm"))
  .settings(
    scalaVersion := scalaV,
    scalaJSProjects := clients,
    pipelineStages := Seq(rjs, digest, scalaJSProd, gzip),
    resolvers ++= Seq(
      "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
      Resolver.jcenterRepo
    ),
    routesGenerator := InjectedRoutesGenerator,
    RjsKeys.paths += ("jsRoutes" -> ("/jsroutes" -> "empty:")),
    RjsKeys.mainModule := "main",
    RjsKeys.webJarCdns := Map.empty,
    DigestKeys.algorithms += "md5",
    includeFilter in digest := "*.js",
    includeFilter in gzip := "*.html" || "*.css" || "*.js",
    libraryDependencies ++= Seq(
      filters,
      cache,
      ws,
      "com.iheart"     %% "ficus"                % "1.2.6",
      "com.netaporter" %% "scala-uri"            % "0.4.14",
      "com.vmunier"    %% "play-scalajs-scripts" % "0.5.0"
    )
  )
  .enablePlugins(PlayScala, SbtWeb)
  .aggregate(clients.map(projectToRef): _*)
  .dependsOn(sharedJvm)

lazy val client = (project in file("js"))
  .settings(
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "org.scala-js"                      %%% "scalajs-dom"   % "0.9.1",
      "com.lihaoyi"                       %%% "upickle"       % "0.4.2",
      "com.github.julien-truffaut"        %%% "monocle-macro" % "1.2.2",
      "com.github.japgolly.scalajs-react" %%% "core"          % "0.11.2"
    ),
    jsDependencies ++= Seq(
      "org.webjars.bower" % "react" % reactV
        / "react-with-addons.js"
        minified "react-with-addons.min.js"
        commonJSName "React",
      "org.webjars.bower" % "react" % reactV
        / "react-dom.js"
        minified "react-dom.min.js"
        dependsOn "react-with-addons.js"
        commonJSName "ReactDOM",
      "org.webjars.bower" % "react" % reactV
        / "react-dom-server.js"
        minified "react-dom-server.min.js"
        dependsOn "react-dom.js"
        commonJSName "ReactDOMServer"
    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .jsSettings(
    libraryDependencies ++= Seq(
      "com.lihaoyi"          %%% "scalatags"        % scalatagsV
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.lihaoyi"          %% "scalatags"        % scalatagsV
    )
  )

  .jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs  = shared.js

onLoad in Global := (Command
  .process("project server", _: State)) compose (onLoad in Global).value
