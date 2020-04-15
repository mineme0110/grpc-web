resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

scalaVersion := "2.12.10"

val grpcVersion = "1.28.1"


libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
  "io.grpc" % "grpc-netty" % grpcVersion
)


PB.targets in Compile := Seq(
  scalapb.gen(grpc = true) -> (sourceManaged in Compile).value,
  scalapb.grpc_web.GrpcWebWithMetadataCodeGenerator -> (sourceManaged in Compile).value,
)





fork := true

cancelable in Global := true

connectInput := true
