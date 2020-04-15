import Settings.stdSettings

val grpcVersion = "1.28.1"

val Scala213 = "2.13.1"

val Scala212 = "2.12.10"

ThisBuild / resolvers += Resolver.sonatypeRepo("snapshots")

ThisBuild / scalaVersion := Scala213

ThisBuild / crossScalaVersions := Seq(Scala212, Scala213)

skip in publish := true

sonatypeProfileName := "com.thesamet"

inThisBuild(
  List(
    organization := "com.thesamet.scalapb.grpc-web",
    homepage := Some(url("https://github.com/scalameta/sbt-scalafmt")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "thesamet",
        "Nadav Samet",
        "thesamet@gmail.com",
        url("https://www.thesamet.com")
      )
    )
  )
)



lazy val codeGen = project
  .in(file("code-gen"))
  .enablePlugins(BuildInfoPlugin)
  .settings(stdSettings)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "scalapb.grpc-web",
    name := "grpc-web-codegen",
    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %% "compilerplugin" % scalapb.compiler.Version.scalapbVersion
    )
  )

def projDef(name: String, shebang: Boolean) =
  sbt
    .Project(name, new File(name))
    .enablePlugins(AssemblyPlugin)
    .dependsOn(codeGen)
    .settings(stdSettings)
    .settings(
      assemblyOption in assembly := (assemblyOption in assembly).value.copy(
        prependShellScript = Some(
          sbtassembly.AssemblyPlugin.defaultUniversalScript(shebang = shebang)
        )
      ),
      skip in publish := true,
      Compile / mainClass := Some("scalapb.grpc_web.GrpcWebWithMetadataCodeGenerator")
    )

lazy val protocGenWebUnix = projDef("protoc-gen-web-unix", shebang = true)

lazy val protocGenWebWindows =
  projDef("protoc-gen-web-windows", shebang = false)

lazy val protocGenWeb = project
  .settings(
    crossScalaVersions := List(Scala213),
    name := "protoc-gen-web",
    publishArtifact in (Compile, packageDoc) := false,
    publishArtifact in (Compile, packageSrc) := false,
    crossPaths := false,
    addArtifact(
      Artifact("protoc-gen-web", "jar", "sh", "unix"),
      assembly in (protocGenWebUnix, Compile)
    ),
    addArtifact(
      Artifact("protoc-gen-web", "jar", "bat", "windows"),
      assembly in (protocGenWebWindows, Compile)
    ),
    autoScalaLibrary := false
  )

