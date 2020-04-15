resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.31")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.2")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.10.2"


unmanagedSourceDirectories in Compile ++= Seq(
  baseDirectory.value / ".." / ".."/ "code-gen" / "src" / "main" / "scala",
  baseDirectory.value / ".." / ".."/ "code-gen" / "src" / "main" / "scala-2.12"
)

//libraryDependencies += "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen" % "0.2.0+8-beeaf3a7+20200410-1544-SNAPSHOT"
