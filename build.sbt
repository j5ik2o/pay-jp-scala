val coreSettings = Seq(
  sonatypeProfileName := "com.github.j5ik2o",
  organization := "com.github.j5ik2o",
  scalaVersion := "2.12.6",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-encoding",
    "UTF-8",
    "-language:existentials",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-language:higherKinds",
    "-Ywarn-adapted-args" // Warn if an argument list is modified to match the receiver
    ,
    "-Ywarn-dead-code" // Warn when dead code is identified.
    ,
    "-Ywarn-inaccessible" // Warn about inaccessible types in method signatures.
    ,
    "-Ywarn-infer-any" // Warn when a type argument is inferred to be `Any`.
    ,
    "-Ywarn-nullary-override" // Warn when non-nullary `def f()' overrides nullary `def f'
    ,
    "-Ywarn-nullary-unit" // Warn when nullary methods return Unit.
    ,
    "-Ywarn-numeric-widen" // Warn when numerics are widened.
    ,
    "-Ywarn-unused" // Warn when local and private vals, vars, defs, and types are are unused.
    ,
    "-Xmax-classfile-name",
    "200"
  ),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ =>
    false
  },
  pomExtra := {
    <url>https://github.com/j5ik2o/pay-jp-scala</url>
      <licenses>
        <license>
          <name>The MIT License</name>
          <url>http://opensource.org/licenses/MIT</url>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:j5ik2o/pay-jp-scala.git</url>
        <connection>scm:git:github.com/j5ik2o/pay-jp-scala</connection>
        <developerConnection>scm:git:git@github.com:j5ik2o/pay-jp-scala.git</developerConnection>
      </scm>
      <developers>
        <developer>
          <id>j5ik2o</id>
          <name>Junichi Kato</name>
        </developer>
      </developers>
  },
  publishTo := sonatypePublishTo.value,
  credentials := {
    val ivyCredentials = (baseDirectory in LocalRootProject).value / ".credentials"
    Credentials(ivyCredentials) :: Nil
  },
  scalafmtOnCompile in ThisBuild := true,
  scalafmtTestOnCompile in ThisBuild := true
)

val circeVersion    = "0.9.3"
val akkaHttpVersion = "10.1.1"
val akkaVersion     = "2.5.11"

lazy val library = (project in file("library")).settings(
  coreSettings ++ Seq(
    name := "pay-jp-scala",
    libraryDependencies ++= Seq(
      "org.scalatest"     %% "scalatest"         % "3.0.5" % Test,
      "commons-codec"     % "commons-codec"      % "1.11",
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-http"         % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor"        % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"       % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j"        % akkaVersion,
      "io.monix"          %% "monix"             % "3.0.0-RC1",
      "com.beachape"      %% "enumeratum"        % "1.5.13",
      "com.chuusai"       %% "shapeless"         % "2.3.3",
      "org.slf4j"         % "slf4j-api"          % "1.7.25",
      "ch.qos.logback"    % "logback-classic"    % "1.2.3" % Test
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
)

lazy val example = (project in file("example")).settings(
  coreSettings ++ Seq(
    name := "pay-jp-scala-example"
  )
) dependsOn library

lazy val `root` = (project in file(".")).settings(
  name := "pay-jp-scala-project"
)
