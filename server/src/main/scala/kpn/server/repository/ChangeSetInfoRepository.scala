package kpn.server.repository

import kpn.api.common.changes.ChangeSetInfo
import kpn.core.util.Log
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class ChangeSetInfoRepository(database: Database) {

  private val log = Log(classOf[ChangeSetInfoRepository])

  def save(changeSetInfo: ChangeSetInfo): Unit = {
    database.changeSets.save(changeSetInfo, log)
  }

  def get(changeSetId: Long): Option[ChangeSetInfo] = {
    database.changeSets.findById(changeSetId, log)
  }

  def all(changeSetIds: Seq[Long]): Seq[ChangeSetInfo] = {
    database.changeSets.findByIds(changeSetIds, log)
  }

  def exists(changeSetId: Long): Boolean = {
    database.changeSets.findById(changeSetId, log).isDefined
  }

  def delete(changeSetId: Long): Unit = {
    database.changeSets.delete(changeSetId, log)
  }
}
