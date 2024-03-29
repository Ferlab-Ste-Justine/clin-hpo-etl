package bio.ferlab.clin.idx.hpo

import bio.ferlab.clin.idx.hpo.HPO.transform
import bio.ferlab.clin.idx.hpo.utils.SparkSessionTrait
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

case class Ancestors(
                      hpo_id: String,
                      name: String
                    )

case class HPO_Output(
                       hpo_id: String,
                       name: String,
                       is_leaf: Boolean,
                       parents: Seq[String] = Nil,
                       compact_ancestors: Seq[Ancestors]
                     ){

}

class HPOSpecs extends AnyFlatSpec with SparkSessionTrait with Serializable with Matchers  {

  "HPO filters" should "return entries in proper format" in {
    import spark.implicits._
    val path = this.getClass.getResource("/hpospecs_terms.json").getFile

    val dataSet = spark.read.json(path).as[HPOEntry]

    val filtered = transform(dataSet)

    val term = filtered.filter(filtered("hpo_id") === "HP:0000164").as[HPO_Output].collect().head

    term shouldBe HPO_Output(
      hpo_id = "HP:0000164",
      name = "Abnormality of the dentition",
      is_leaf = false,
      parents = Seq("Abnormal oral cavity morphology (HP:0000163)"),
      compact_ancestors = Seq(
        Ancestors(hpo_id = "HP:0000234", name = "Abnormality of the head"),
        Ancestors(hpo_id = "HP:0000001", name = "All"),
        Ancestors(hpo_id = "HP:0000153", name = "Abnormality of the mouth"),
        Ancestors(hpo_id = "HP:0000118", name = "Phenotypic abnormality"),
        Ancestors(hpo_id = "HP:0000152", name = "Abnormality of head or neck"),
        Ancestors(hpo_id = "HP:0031816", name = "Abnormal oral morphology"),
        Ancestors(hpo_id = "HP:0000271", name = "Abnormality of the face"),
        Ancestors(hpo_id = "HP:0000163", name = "Abnormal oral cavity morphology")
      )
    )
  }
}
