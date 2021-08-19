name := "clin-hpo-etl"

version := "0.1"

scalaVersion := "2.12.12"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")
val datalakeSpark3Version = "0.0.54"
val spark_version = "3.1.2"

/* Runtime */
libraryDependencies +=  "org.apache.spark" %% "spark-sql" % spark_version % Provided
libraryDependencies += "bio.ferlab" %% "datalake-spark3" % datalakeSpark3Version
libraryDependencies += "org.elasticsearch" %% "elasticsearch-spark-30" % "7.12.0"

/* Test */
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"
libraryDependencies += "org.apache.spark" %% "spark-hive" % spark_version % "test"
libraryDependencies ++= Seq("junit" % "junit" % "4.8.1" % "test")
test / parallelExecution := false
fork := true

assembly / test:= {}

assembly / assemblyShadeRules := Seq(
  ShadeRule.rename("shapeless.**" -> "shadeshapless.@1").inAll
)

assembly / assemblyMergeStrategy := {
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case "META-INF/native/libnetty_transport_native_epoll_x86_64.so" => MergeStrategy.last
  case "META-INF/DISCLAIMER" => MergeStrategy.last
  case "mozilla/public-suffix-list.txt" => MergeStrategy.last
  case "overview.html" => MergeStrategy.last
  case "git.properties" => MergeStrategy.discard
  case "mime.types" => MergeStrategy.first
  case PathList("scala", "annotation", "nowarn.class" | "nowarn$.class") => MergeStrategy.first
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}
assemblyJarName in assembly := "clin-hpo-etl.jar"
