import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

ThisBuild / crossScalaVersions := Seq("2.13.6")

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

ThisBuild / githubWorkflowPublishTargetBranches := Seq(RefPredicate.StartsWith(Ref.Tag("v")))

ThisBuild / githubWorkflowBuildPreamble ++= Seq(WorkflowStep.Use(
  UseRef.Public("actions", "setup-node", "v1"),
  Map(
    "node-version" -> "14"
  )
))

ThisBuild / githubWorkflowBuild ++= Seq(
  WorkflowStep.Sbt(
    List("test", "npmPackageInstall"),
    name = Some("Install artifacts to npm"),
  )
)

ThisBuild / githubWorkflowPublishPreamble ++=  Seq(WorkflowStep.Use(
  UseRef.Public("actions", "setup-node", "v1"),
  Map(
    "node-version" -> "14"
  )
))

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    List("test", "npmPackagePublish"),
    name = Some("Publish artifacts to npm"),
    env = Map(
      "NODE_AUTH_TOKEN" -> "${{ secrets.NPM_TOKEN }}"
    )
  )
)

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
  .enablePlugins(NpmPackagePlugin)
  .settings(
    name := "circuit-scala",
    npmPackageAuthor := "Christopher Davenport",
    npmPackageDescription := "CircuitBreaker is used to provide stability and prevent cascading failures in distributed systems.",
    npmPackageKeywords := Seq(
      "circuit breaker",
      "circuit-breaker",
      "fail-fast",
      "circuit",
      "breaker",
      "rate-limiting"
    ),
    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
    libraryDependencies ++= Seq(
      "io.chrisdavenport" %%% "circuit" % "0.5.0-M2",
      "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,
    ),
  )