package kpn.core.repository

import kpn.core.db.ChangeSetInfoDoc
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.changeSetInfoDocFormat
import kpn.shared.changes.ChangeSetInfo

class ChangeSetInfoRepositoryImpl(database: Database) extends ChangeSetInfoRepository {

  override def save(changeSetInfo: ChangeSetInfo): Unit = {
    val id = docId(changeSetInfo.id)
    val doc = ChangeSetInfoDoc(id, changeSetInfo)
    database.delete(id)
    database.save(id, changeSetInfoDocFormat.write(doc))
  }

  override def get(changeSetId: Long): Option[ChangeSetInfo] = {
    val doc: Option[ChangeSetInfoDoc] = database.optionGet(docId(changeSetId), Couch.defaultTimeout).map(changeSetInfoDocFormat.read)
    doc.map(_.changeSetInfo)
  }

  override def all(changeSetIds: Seq[Long], stale: Boolean): Seq[ChangeSetInfo] = {
    changeSetIds.sliding(40, 40).flatMap { changeSetIdsSubset =>
      val ids = changeSetIdsSubset.map(docId)
      val docs: Seq[ChangeSetInfoDoc] = database.objectsWithIds(ids, Couch.defaultTimeout, stale).map(changeSetInfoDocFormat.read)
      docs.map(_.changeSetInfo)
    }.toSeq
  }

  override def exists(changeSetId: Long): Boolean = {
    database.currentRevision(docId(changeSetId), Couch.defaultTimeout).isDefined
  }

  override def delete(changeSetId: Long): Unit = {
    database.delete(docId(changeSetId))
  }

  private def docId(changeSetId: Long): String = s"change-set-info:$changeSetId"
}
