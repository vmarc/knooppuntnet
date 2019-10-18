package kpn.server.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.OldDatabase
import kpn.core.db.json.JsonFormats.changeSetInfoFormat
import kpn.shared.changes.ChangeSetInfo
import org.springframework.stereotype.Component

@Component
class ChangeSetInfoRepositoryImpl(oldChangesetDatabase: OldDatabase) extends ChangeSetInfoRepository {

  override def save(changeSetInfo: ChangeSetInfo): Unit = {
    val id = docId(changeSetInfo.id)
    oldChangesetDatabase.delete(id)
    oldChangesetDatabase.save(id, changeSetInfoFormat.write(changeSetInfo))
  }

  override def get(changeSetId: Long): Option[ChangeSetInfo] = {
    oldChangesetDatabase.optionGet(docId(changeSetId), Couch.defaultTimeout).map(changeSetInfoFormat.read)
  }

  override def all(changeSetIds: Seq[Long], stale: Boolean): Seq[ChangeSetInfo] = {
    changeSetIds.sliding(40, 40).flatMap { changeSetIdsSubset =>
      val ids = changeSetIdsSubset.map(docId)
      oldChangesetDatabase.objectsWithIds(ids, Couch.defaultTimeout, stale).map(changeSetInfoFormat.read)
    }.toSeq
  }

  override def exists(changeSetId: Long): Boolean = {
    oldChangesetDatabase.currentRevision(docId(changeSetId), Couch.defaultTimeout).isDefined
  }

  override def delete(changeSetId: Long): Unit = {
    oldChangesetDatabase.delete(docId(changeSetId))
  }

  private def docId(changeSetId: Long): String = s"change-set-info:$changeSetId"
}
