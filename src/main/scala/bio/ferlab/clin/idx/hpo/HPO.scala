package bio.ferlab.clin.idx.hpo

import org.apache.spark.sql.{Dataset, SparkSession}
import org.elasticsearch.spark.sql.sparkDatasetFunctions

object HPO extends App {
  implicit val spark: SparkSession = SparkSession.builder
    .master("local")
    //    .config("es.index.auto.create", "true")
    .appName(s"HPOIndexer").getOrCreate()

  case class HPOEntry(id: String, name: String, parents: Seq[String] = Seq.empty, ancestors: Seq[AncestorData] = Seq.empty, is_leaf: Boolean)

  case class AncestorData(id: String, name: String, parents: Seq[String] = Seq.empty)

  case class CompactAncestorData(id: String, name: String)

  case class CompactHPOEntry(id: String, name: String, parents: Seq[String], ancestors: Seq[CompactAncestorData])

  private def compactEntry(entry: HPOEntry): CompactHPOEntry = {
    CompactHPOEntry(entry.id, entry.name, entry.parents, entry.ancestors.map(compactAncestor))
  }

  private def compactAncestor(entry: AncestorData): CompactAncestorData = {
    CompactAncestorData(entry.id, entry.name)
  }

  val matches = Seq(
    "HP:0001197",
    "HP:0001507",
    "HP:0000478",
    "HP:0000598",
    "HP:0001574",
    "HP:0001626",
    "HP:0002086",
    "HP:0000924",
    "HP:0002086",
    "HP:0000924",
    "HP:0003011",
    "HP:0000119",
    "HP:0025031",
    "HP:0000152",
    "HP:0000707"
  )

  import spark.implicits._

  //  println(filtered.count)
  private val dataSet = ReadHPOData.fromJson(spark).as[HPOEntry]
  private val filteredDataSet = filterByAncestors(dataSet, matches)

  filteredDataSet.show()
  // Save to ES
  filteredDataSet.toDF.saveToEs("sparktest")


  /**
   * Filters a Dataset to remove all the entries without any ancestor found in @param matches.
   *
   * @param dataSet Dataset to filter
   * @param matches Contains all the ids of the ancestors to keep
   * @return Filtered Dataset
   */
  def filterByAncestors(dataSet: Dataset[HPOEntry], matches: Seq[String]): Dataset[CompactHPOEntry] = {
    dataSet.filter(data =>
      data.ancestors.map(ancestor => ancestor.id)
        .intersect(matches)
        .nonEmpty)
      .map(compactEntry)
  }
}



