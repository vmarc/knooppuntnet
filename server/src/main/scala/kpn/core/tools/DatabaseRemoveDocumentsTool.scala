package kpn.core.tools

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import spray.json.JsString

object DatabaseRemoveDocumentsTool {
  def main(args: Array[String]): Unit = {
    new DatabaseRemoveDocumentsTool().removeInChanges()
    // new DatabaseRemoveDocumentsTool().removeInMaster()
  }
}

class DatabaseRemoveDocumentsTool {

  def removeInChanges(): Unit = {
    Couch.executeIn("changes") { database =>
      Seq(
        "change-set"
      ).foreach { documentType =>
        remove(database, documentType)
      }
    }
  }

  def removeInMaster(): Unit = {
    Couch.executeIn("master") { database =>
      Seq(
        "network",
        "network-gpx",
        "network-timestamp",
        "node",
        "route"
      ).foreach { documentType =>
        remove(database, documentType)
      }
    }
  }

  def remove(database: Database, documentType: String): Unit = {
    val startKey = documentType + ":"
    val endKey = documentType + ":999999999999"
    val keys = database.old.keys(startKey, endKey, Couch.defaultTimeout).flatMap {
      case JsString(key) => Some(key)
      case _ => None
    }
    println(s"$documentType ${keys.size} documents")
    val slided = keys.sliding(250, 250).toArray
    slided.zipWithIndex.foreach { case (keyss, index) =>
      println(s"${index + 1}/${slided.length} keyss.size=${keyss.size}")
      database.deleteDocsWithIds(keyss)
    }
  }
}
