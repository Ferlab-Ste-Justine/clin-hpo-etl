name := "clin-etl-indexer"

version := "0.1"

scalaVersion := "2.12.12"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

val spark_version = "3.1.2"

/* Runtime */
libraryDependencies +=  "org.apache.spark" %% "spark-sql" % spark_version
libraryDependencies += "org.elasticsearch" %% "elasticsearch-spark-30" % "7.12.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "3.3.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "3.3.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.2.2"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-bundle" % "1.11.563"

/* Test */
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"
libraryDependencies += "org.apache.spark" %% "spark-hive" % spark_version % "test"
libraryDependencies ++= Seq("junit" % "junit" % "4.8.1" % "test")


test in assembly := {}

assemblyMergeStrategy in assembly := {
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case "META-INF/native/libnetty_transport_native_epoll_x86_64.so" => MergeStrategy.last
  case "META-INF/DISCLAIMER" => MergeStrategy.last
  case "mozilla/public-suffix-list.txt" => MergeStrategy.last
  case "overview.html" => MergeStrategy.last
  case "git.properties" => MergeStrategy.discard
  case "mime.types" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case x => MergeStrategy.first
}
assemblyJarName in assembly := "clin-etl-indexer.jar"
