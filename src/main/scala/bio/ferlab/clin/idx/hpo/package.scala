package bio.ferlab.clin.idx

import bio.ferlab.clin.idx.hpo.HPO.AncestorData

package object hpo {

  val requiredRootTerms = Seq(
    "HP:0001197", //Abnormality of prenatal development or birth
    "HP:0001507", //Growth abnormality
    "HP:0000478", //Abnormality of the eye
    "HP:0000598", //Abnormality of the ear
    "HP:0001574", //Abnormality of the integument - missing!!
    "HP:0001626", //Abnormality of the cardiovascular system
    "HP:0033127", //Abnormality of the musculoskeletal system
    "HP:0002086", //Abnormality of the respiratory system
    "HP:0000119", //Abnormality of the genitourinary system
    "HP:0025031", //Abnormality of the digestive system
    "HP:0000152", //Abnormality of head or neck
    "HP:0000707" //Abnormality of the nervous system
  )

  val topRootTerm: AncestorData = AncestorData(id = "HP:0000118", name = "Phenotypic abnormality", parents = Seq("All (HP:0000001)"))
}
