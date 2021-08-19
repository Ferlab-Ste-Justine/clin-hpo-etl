package bio.ferlab.clin.idx.hpo

import bio.ferlab.datalake.spark3.elasticsearch.{ElasticSearchClient, Indexer}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.{col, collect_list, explode, struct}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.slf4j.LoggerFactory

object HPO extends App {

  Logger.getLogger("HPO").setLevel(Level.INFO)
  val log = LoggerFactory.getLogger(this.getClass)

  if(args.length >= 4) {
    implicit val spark: SparkSession = SparkSession.builder
    .config("es.index.auto.create", "true")
    .getOrCreate()
    import spark.implicits._
    implicit val esClient: ElasticSearchClient = new ElasticSearchClient(spark.conf.get("es.nodes").split(',').head)

    val Array(inputPath, indexName, releaseId) = args

    val dataSet = spark.read.parquet(inputPath).as[HPOEntry]
    val filteredDataSet = transform(dataSet)

    val templatePath = HPO.getClass.getResource("/template/hpo.json").getFile
    val job = new Indexer("index", templatePath, indexName, s"${indexName}_$releaseId")
    job.run(filteredDataSet.toDF)
  } else{
    log.error("HPO terms Input Path, index name and release id are missing")
    System.exit(-1)
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




case class HPOEntry(id: String, name: String, parents: Seq[String] = Seq.empty, ancestors: Seq[AncestorData] = Seq.empty, is_leaf: Boolean)

case class AncestorData(id: String, name: String, parents: Seq[String] = Seq.empty)