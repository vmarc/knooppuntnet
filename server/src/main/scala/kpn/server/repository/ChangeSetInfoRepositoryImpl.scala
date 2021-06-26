package kpn.server.repository

import kpn.api.common.changes.ChangeSetInfo
import kpn.core.database.doc.ChangeSetInfoDoc
import kpn.core.mongo.Database
import kpn.core.util.Log
import org.springframework.stereotype.Component

object ChangeSetInfoRepositoryImpl {

  private case class ViewResult(rows: Seq[ViewResultRow])

  private case class ViewResultRow(doc: ChangeSetInfoDoc)

}

@Component
class ChangeSetInfoRepositoryImpl(
  database: Database,
  // old:
  changesetDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends ChangeSetInfoRepository {

  private val log = Log(classOf[ChangeSetInfoRepositoryImpl])

  override def save(changeSetInfo: ChangeSetInfo): Unit = {
    if (mongoEnabled) {
      database.changeSets.save(changeSetInfo, log)
    }
    else {
      val id = docId(changeSetInfo.id)
      changesetDatabase.save(ChangeSetInfoDoc(id, changeSetInfo))
    }
  }

  override def get(changeSetId: Long): Option[ChangeSetInfo] = {
    if (mongoEnabled) {
      database.changeSets.findById(changeSetId, log)
    }
    else {
      changesetDatabase.docWithId(docId(changeSetId), classOf[ChangeSetInfoDoc]).map(_.changeSetInfo)
    }
  }

  override def all(changeSetIds: Seq[Long], stale: Boolean): Seq[ChangeSetInfo] = {
    if (mongoEnabled) {
      database.changeSets.findByIds(changeSetIds, log)
    }
    else {
      import ChangeSetInfoRepositoryImpl._
      changeSetIds.sliding(40, 40).flatMap { changeSetIdsSubset =>
        val ids = changeSetIdsSubset.map(docId)
        val rows = changesetDatabase.docsWithIds(ids, classOf[ViewResult], stale).rows
        rows.filter(_.doc != null).map(_.doc.changeSetInfo)
      }.toSeq
    }
  }

  override def exists(changeSetId: Long): Boolean = {
    if (mongoEnabled) {
      database.changeSets.findById(changeSetId, log).isDefined
    }
    else {
      changesetDatabase.revision(docId(changeSetId)).isDefined
    }
  }

  override def delete(changeSetId: Long): Unit = {
    if (mongoEnabled) {
      database.changeSets.delete(changeSetId, log)
    }
    else {
      changesetDatabase.deleteDocWithId(docId(changeSetId))
    }
  }

  private def docId(changeSetId: Long): String = s"change-set-info:$changeSetId"
}
