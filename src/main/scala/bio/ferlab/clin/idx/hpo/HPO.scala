package bio.ferlab.clin.idx.hpo

import org.apache.log4j.{Level, Logger}
import org.slf4j.LoggerFactory
import org.apache.spark.sql.functions.{col, collect_list, explode, struct}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.elasticsearch.spark.sql.sparkDatasetFunctions

object HPO extends App {

  Logger.getLogger("ferlab").setLevel(Level.INFO)
  val log = LoggerFactory.getLogger(this.getClass)

  implicit val spark: SparkSession = SparkSession.builder
    .getOrCreate()

  case class HPOEntry(id: String, name: String, parents: Seq[String] = Seq.empty, ancestors: Seq[AncestorData] = Seq.empty, is_leaf: Boolean)

  case class AncestorData(id: String, name: String, parents: Seq[String] = Seq.empty)
  import spark.implicits._

  if(args.length >= 2) {
    val Array(inputPath, indexName) = args
    val dataSet = spark.read.parquet(inputPath).as[HPOEntry]
    val filteredDataSet = transform(dataSet)

    filteredDataSet.toDF.saveToEs(indexName)
  } else{
    log.error("HPO terms Input Path missing")
  }

  /**
   * Transform the DataSet into to desired output DataFrame.
   *
   * @param dataSet Dataset to transform
   * @return Transformed DataFrame
   */
  def transform(dataSet: Dataset[HPOEntry]): DataFrame = {
    dataSet
      .select(col("*"), explode(col("ancestors")).as("ancestor")).drop("ancestors")
      .select(
        col("id").as("hpo_id"),
        col("name").as("hpo_name"),
        col("parents"),
        col("is_leaf"),
        col("ancestor.id").as("id"),
        col("ancestor.name").as("name"))
      .groupBy(col("hpo_id"), col("hpo_name"), col("parents"), col("is_leaf"))
      .agg(
        collect_list(
          struct("id", "name")
        ) as "compact_ancestors"
      )
      .withColumnRenamed("hpo_id", "id")
      .withColumnRenamed("hpo_name", "name")
  }
}




