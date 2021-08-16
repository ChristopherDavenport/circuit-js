import org.scalajs.sbtplugin.Stage
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

ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(
    List("test", "npmPackageInstall"),
    name = Some("Install artifacts to npm"),
  )
)

ThisBuild / githubWorkflowPublishPreamble ++= Seq(
  WorkflowStep.Use(
    UseRef.Public("actions", "setup-node", "v1"),
    Map(
      "node-version" -> "14",
    ),
  )
)

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Run(
    List("mkdir -p core/target/scala-2.13/npm-package"),
    name = Some("Create Target Directory So That we have a location for config")
  ), // We run to make sure directory exists
  WorkflowStep.Run(
    List("echo \"//registry.npmjs.org/:_authToken=\\${NPM_TOKEN}\" > core/target/scala-2.13/npm-package/.npmrc" ),
    name = Some("Create npmrc so that the token is used for npm publication")
  ), // We write a config for the directory for npm
  WorkflowStep.Sbt(
    List("npmPackagePublish"),
    name = Some("Publish artifacts to npm"),
    env = Map(
      "NPM_TOKEN" -> "${{ secrets.NPM_TOKEN }}" // https://docs.npmjs.com/using-private-packages-in-a-ci-cd-workflow#set-the-token-as-an-environment-variable-on-the-cicd-server
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
    npmPackageStage := Stage.FullOpt,
    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
    libraryDependencies ++= Seq(
      "io.chrisdavenport" %%% "circuit" % "0.5.0-M2",
      "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,
    ),
  )