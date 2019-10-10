package kpn.core.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.changeSetInfoFormat
import kpn.shared.changes.ChangeSetInfo

class ChangeSetInfoRepositoryImpl(database: Database) extends ChangeSetInfoRepository {

  override def save(changeSetInfo: ChangeSetInfo): Unit = {
    val id = docId(changeSetInfo.id)
    database.delete(id)
    database.save(id, changeSetInfoFormat.write(changeSetInfo))
  }

  override def get(changeSetId: Long): Option[ChangeSetInfo] = {
    database.optionGet(docId(changeSetId), Couch.defaultTimeout).map(changeSetInfoFormat.read)
  }

  override def all(changeSetIds: Seq[Long], stale: Boolean): Seq[ChangeSetInfo] = {
    changeSetIds.sliding(40, 40).flatMap { changeSetIdsSubset =>
      val ids = changeSetIdsSubset.map(docId)
      database.objectsWithIds(ids, Couch.defaultTimeout, stale).map(changeSetInfoFormat.read)
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
