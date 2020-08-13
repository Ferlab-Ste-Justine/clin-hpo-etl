package bio.ferlab.clin.idx.hpo.utils

import org.apache.spark.sql.SparkSession


trait SparkSessionTrait {

  implicit lazy val spark: SparkSession = SparkSession.builder()
    .master("local")
    .appName(s"HPOIndexerSpecs")
    .getOrCreate()
}
