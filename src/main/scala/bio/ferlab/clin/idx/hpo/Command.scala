package bio.ferlab.clin.idx.hpo

import org.apache.spark.sql.{DataFrame, SparkSession}

abstract class Command[Input, Output] {
  def execute(input: Input): Output
}

abstract class ReadHPODataCommand extends Command[Unit, DataFrame]()

object ReadHPOData{
  def fromJson(spark: SparkSession): DataFrame = {
    spark.read.json(getClass.getResource("./hpo_terms_2021_08_02.json").getFile)
  }

  def fromParquet(path: String)(spark: SparkSession): DataFrame ={
    spark.read.parquet(path)
  }
}


class ReadHPODataFromUpstream extends ReadHPODataCommand {
  override def execute(input: Unit): DataFrame = {
    throw new FunctionNotImplementedException()
  }
}
