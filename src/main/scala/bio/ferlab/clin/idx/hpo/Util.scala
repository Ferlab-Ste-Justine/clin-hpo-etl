package bio.ferlab.clin.idx.hpo

import scala.util.Properties

object Util {
  def getConfiguration(key: String, default: String): String = {
    Properties.envOrElse(key, Properties.propOrElse(key, default))
  }
}
