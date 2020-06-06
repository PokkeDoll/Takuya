name := "Takuya"

version := "1.0"

scalaVersion := "2.13.1"

resolvers += "papermc" at "https://papermc.io/repo/repository/maven-public/"

libraryDependencies ++= Seq(
  "com.destroystokyo.paper" % "paper-api" % "1.12.2-R0.1-SNAPSHOT",
  "com.zaxxer" % "HikariCP" % "3.4.2"
)

val libs = Seq(
  "lib/HikariCP-3.4.2.jar",
  "lib/scala-library-2.13.1.jar"
)

packageOptions in (Compile, packageBin) +=
  Package.ManifestAttributes("Class-Path" -> libs.tail.mkString(" "))

artifactName :={(sv: ScalaVersion,module: ModuleID, artifact: Artifact) => "Takuya" + module.revision + "." + artifact.extension}
