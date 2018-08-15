package kpn.core.repository

import kpn.core.db.ReviewDoc
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.reviewDocFormat
import kpn.shared.ReplicationId
import kpn.shared.changes.Review

class ReviewRepositoryImpl(database: Database) extends ReviewRepository {

  override def save(changeSetId: Long, replicationId: ReplicationId, review: Review): Unit = {
    val id = makeId(changeSetId, replicationId)
    val doc = getDoc(id) match {
      case Some(reviewDoc) => ReviewDoc(id, reviewDoc.reviews ++ Seq(review), reviewDoc._rev)
      case None => ReviewDoc(id, Seq(review), None)
    }
    database.save(id, reviewDocFormat.write(doc))
  }

  override def get(changeSetId: Long, replicationId: ReplicationId): Seq[Review] = {
    val id = makeId(changeSetId, replicationId)
    getDoc(id) match {
      case Some(reviewDoc) => reviewDoc.reviews
      case None => Seq()
    }
  }

  private def makeId(changeSetId: Long, replicationId: ReplicationId): String = changeSetId + ":" + replicationId.key

  private def getDoc(id: String): Option[ReviewDoc] = {
    database.optionGet(id, Couch.uiTimeout).map(jsDoc => reviewDocFormat.read(jsDoc))
  }
}
