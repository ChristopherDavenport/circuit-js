import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

ThisBuild / crossScalaVersions := Seq("2.13.6")

ThisBuild / testFrameworks += new TestFramework("munit.Framework")


val catsV = "2.6.1"
val catsEffectV = "3.1.1"
val fs2V = "3.0.6"
val circeV = "0.14.1"
val doobieV = "1.0.0-M5"
val munitCatsEffectV = "1.0.5"


// Projects
lazy val `circuit-js` = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .aggregate(core)

lazy val core = project.in(file("core"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    name := "circuit-js",
    libraryDependencies ++= Seq(
      "io.chrisdavenport" %%% "circuit" % "0.5.0-M2",
      "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,
    ),
    // scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))
  )