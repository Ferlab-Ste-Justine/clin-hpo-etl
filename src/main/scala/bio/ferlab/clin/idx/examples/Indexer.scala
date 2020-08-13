package bio.ferlab.clin.idx.examples

import org.apache.spark.sql.SparkSession
import org.elasticsearch.spark.sql.sparkDatasetFunctions

object Indexer extends App {

  implicit val spark: SparkSession = SparkSession.builder
    //    .master("local")
    .config("es.index.auto.create", "true")
    .appName(s"Indexer").getOrCreate()

  val df = spark.read.json("s3a://spark/ectrat2/extract/") //.withColumn("id", sha1(functions.concat($"chromosome", $"start", $"reference", $"alternate")))



  df.saveToEs("spark3/docs", Map("es.mapping.id" -> "id"))


}
