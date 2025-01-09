package kpn.server.repository

import kpn.core.doc.BaseNodeDoc
import kpn.database.base.Database

class BaseNodeRepositoryImpl(database: Database) extends BaseNodeRepository {
  def saveBaseNode(baseNode: BaseNodeDoc): Unit = {
    database.baseNodes.save(baseNode)
  }
}
