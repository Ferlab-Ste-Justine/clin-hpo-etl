package bio.ferlab.clin.idx.hpo

import org.apache.spark.sql.{DataFrame, SparkSession}

abstract class Command[Input, Output] {
  def execute(input: Input): Output
}

abstract class ReadHPODataCommand extends Command[Unit, DataFrame]()

object ReadHPOData{
  def fromJson(spark: SparkSession): DataFrame = {
    spark.read.json(getClass.getResource("./hpo_terms.json").getFile)
  }
}


class ReadHPODataFromUpstream extends ReadHPODataCommand {
  override def execute(input: Unit): DataFrame = {
    throw new FunctionNotImplementedException()
  }
}