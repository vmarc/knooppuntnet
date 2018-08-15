package kpn.core.db.views

import kpn.core.db.couch.Couch
import spray.json.DeserializationException
import spray.json.JsString

object DeleteChangesDemo {

  def main(args: Array[String]): Unit = {

    Couch.executeIn("changes3") { database =>

      val keys = database.keys("change-set:", "change-set:9999999999999999", Couch.batchTimeout)

      println(s"keys=${keys.size}")

      val keyStrings: Seq[String] = keys.map {
        case JsString(string) => string
        case _ => throw DeserializationException("xx")
      }

      keyStrings.foreach { key =>
        println(key)
        database.delete(key)
      }
    }
  }
}
