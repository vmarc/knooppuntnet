package kpn.server.repository

import kpn.core.database.Database
import kpn.core.database.doc.ChangeSetInfoDoc
import kpn.shared.changes.ChangeSetInfo
import org.springframework.stereotype.Component

object ChangeSetInfoRepositoryImpl {

  private case class ViewResult(rows: Seq[ViewResultRow])

  private case class ViewResultRow(doc: ChangeSetInfoDoc)

}

@Component
class ChangeSetInfoRepositoryImpl(changesetDatabase: Database) extends ChangeSetInfoRepository {

  override def save(changeSetInfo: ChangeSetInfo): Unit = {
    val id = docId(changeSetInfo.id)
    changesetDatabase.deleteDocWithId(id)
    changesetDatabase.save(ChangeSetInfoDoc(id, changeSetInfo))
  }

  override def get(changeSetId: Long): Option[ChangeSetInfo] = {
    changesetDatabase.docWithId(docId(changeSetId), classOf[ChangeSetInfoDoc]).map(_.changeSetInfo)
  }

  override def all(changeSetIds: Seq[Long], stale: Boolean): Seq[ChangeSetInfo] = {
    import ChangeSetInfoRepositoryImpl._
    changeSetIds.sliding(40, 40).flatMap { changeSetIdsSubset =>
      val ids = changeSetIdsSubset.map(docId)
      changesetDatabase.docsWithIds(ids, classOf[ViewResult], stale).rows.map(_.doc.changeSetInfo)
    }.toSeq
  }

  override def exists(changeSetId: Long): Boolean = {
    changesetDatabase.revision(docId(changeSetId)).isDefined
  }

  override def delete(changeSetId: Long): Unit = {
    changesetDatabase.deleteDocWithId(docId(changeSetId))
  }

  private def docId(changeSetId: Long): String = s"change-set-info:$changeSetId"
}
