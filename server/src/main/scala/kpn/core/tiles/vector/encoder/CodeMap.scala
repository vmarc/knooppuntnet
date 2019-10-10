package kpn.core.tiles.vector.encoder

import scala.collection.immutable.ListMap

class CodeMap {

  private var codeMap = ListMap[String, Int]()

  def code(key: String): Int = {
    codeMap.get(key) match {
      case Some(code) => code
      case None =>
        val code = codeMap.size
        codeMap = codeMap + (key -> code)
        code
    }
  }

  def keys: Seq[String] = codeMap.keys.toSeq

}
