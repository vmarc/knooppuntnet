package kpn.server.repository

import kpn.api.common.changes.ChangeSetInfo
import kpn.core.mongo.Database
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class ChangeSetInfoRepositoryImpl(database: Database) extends ChangeSetInfoRepository {

  private val log = Log(classOf[ChangeSetInfoRepositoryImpl])

  override def save(changeSetInfo: ChangeSetInfo): Unit = {
    database.changeSets.save(changeSetInfo, log)
  }

  override def get(changeSetId: Long): Option[ChangeSetInfo] = {
    database.changeSets.findById(changeSetId, log)
  }

  override def all(changeSetIds: Seq[Long]): Seq[ChangeSetInfo] = {
    database.changeSets.findByIds(changeSetIds, log)
  }

  override def exists(changeSetId: Long): Boolean = {
    database.changeSets.findById(changeSetId, log).isDefined
  }

  override def delete(changeSetId: Long): Unit = {
    database.changeSets.delete(changeSetId, log)
  }
}
