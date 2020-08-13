package bio.ferlab.clin.idx.hpo

import bio.ferlab.clin.idx.hpo.utils.SparkSessionTrait
import org.scalatest.flatspec.AnyFlatSpec

class HPOSpecs extends AnyFlatSpec with SparkSessionTrait with Serializable {

  "HPO filters" should "Return filtered entries" in {
    import spark.implicits._
    val path = this.getClass.getResource("/hpospecs_terms.json").getFile

    val dataSet = spark.read.json(path).as[HPO.HPOEntry]
    println(dataSet.count)
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

    val filtered = dataSet.filter(data =>
      data.ancestors.map(ancestor => ancestor.id)
        .intersect(matches)
        .nonEmpty)
      .map(compactEntry)

    filtered.show()
  }


  private def compactEntry(entry: HPO.HPOEntry): HPO.CompactHPOEntry = {
    HPO.CompactHPOEntry(entry.id, entry.name, entry.parents, entry.ancestors.map(compactAncestor))
  }

  private def compactAncestor(entry: HPO.AncestorData): HPO.CompactAncestorData = {
    HPO.CompactAncestorData(entry.id, entry.name)
  }
}
