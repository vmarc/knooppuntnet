package kpn.core.tools.support

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import spray.json.JsArray
import spray.json.JsObject
import spray.json.JsString
import spray.json.deserializationError

object CopyChangeSetInfoTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("changes1") { sourceDatabase =>
      Couch.executeIn("changesets") { targetDatabase =>
        new CopyChangeSetInfoTool(sourceDatabase, targetDatabase).copy()
      }
    }
  }
}

class CopyChangeSetInfoTool(sourceDatabase: Database, targetDatabase: Database) {

  private val sourceRepo = new ChangeSetInfoRepositoryImpl(sourceDatabase)
  private val targetRepo = new ChangeSetInfoRepositoryImpl(targetDatabase)

  def copy(): Unit = {
    var count = 0
    var ids = readChangeSetIds()
    while (ids.nonEmpty) {
      ids.foreach { changeSetId =>
        count = count + 1
        if (count % 100 == 0) {
          println(s"$count")
        }
        moveChangeSetInfo(changeSetId)
      }
      ids = readChangeSetIds()
    }
  }

  private def readChangeSetIds(): Seq[Long] = {
    val uriHead = Uri(s"_design/ChangesDesign/_view/DocumentView")

    val parameters = Map(
      "startkey" -> """"change-set-info"""",
      "include_docs" -> "false",
      "reduce" -> "false",
      "limit" -> "100"
    )
    val uri = uriHead.withQuery(Query(parameters.seq))

    sourceDatabase.old.getJsValue(uri.toString()) match {
      case result: JsObject =>
        result.getFields("rows") match {
          case Seq(rows: JsArray) =>
            rows.elements.flatMap {
              case row: JsObject =>
                row.getFields("id") match {
                  case Seq(id: JsString) => Some(id.value.split(":")(1).toLong)
                  case _ => None
                }
              case _ => deserializationError("'id' field expected")
            }
          case _ => deserializationError("'rows' field expected")
        }
      case _ => deserializationError("JsObject expected")
    }

  }

  private def moveChangeSetInfo(changeSetId: Long): Unit = {
    sourceRepo.get(changeSetId) match {
      case Some(changeSetInfo) =>
        targetRepo.save(changeSetInfo)
        sourceRepo.delete(changeSetId)
      case _ => println(s"Could not read changesetId $changeSetId")
    }
  }

}
