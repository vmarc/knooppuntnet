package kpn.core.tools.support

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.database.Database
import kpn.core.database.doc.ChangeSetInfoDoc
import kpn.core.database.query.Query
import kpn.core.db.couch.Couch

object MigrateChangeSetInfoTool {

  private case class ExistingDocument(
    _id: String,
    _rev: String,
    id: Long,
    createdAt: Timestamp,
    closedAt: Option[Timestamp],
    open: Boolean,
    commentsCount: Long,
    tags: Tags,
    changeSetInfo: ChangeSetInfo
  ) {
    def toNewDocument: ChangeSetInfoDoc = {
      ChangeSetInfoDoc(
        _id,
        ChangeSetInfo(id, createdAt, closedAt, open, commentsCount, tags),
        Some(_rev)
      )
    }
  }

  private case class ViewResultRow(id: String)

  private case class ViewResult(rows: Seq[ViewResultRow])

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: MigrateChangeSetInfoTool host changesets-database-name")
      System.exit(-1)
    }
    val host = args(0)
    val changeSetsDatabaseName = args(1)


    Couch.executeIn(host, changeSetsDatabaseName) { database =>
      new MigrateChangeSetInfoTool(database).migrate()
      println("Done")
    }
  }
}

class MigrateChangeSetInfoTool(database: Database) {

  import MigrateChangeSetInfoTool._

  def migrate(): Unit = {
    val docIds = readDocIds()
    println(s"Processing ${docIds.size} documents")
    docIds.zipWithIndex.foreach { case (docId, index) =>
      if ((index + 1) % 1000 == 0) {
        println(s"${index + 1}/${docIds.size} ${100 * index / docIds.size}%")
      }
      migrateDocument(docId)
    }
  }

  private def migrateDocument(docId: String): Unit = {
    database.docWithId(docId, classOf[ExistingDocument]).foreach { doc =>
      if (doc.changeSetInfo == null) {
        database.save(doc.toNewDocument)
      }
    }
  }

  private def readDocIds(): Seq[String] = {
    println("Reading doc ids")
    val query = Query("_all_docs", classOf[ViewResult])
      .includeDocs(false)
      .reduce(false)
      .stale(false)
    database.execute(query).rows.map(_.id)
  }
}
