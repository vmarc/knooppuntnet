package kpn.server.repository

import kpn.core.doc.BaseNodeDoc

trait BaseNodeRepository {
  def saveBaseNode(baseNode: BaseNodeDoc): Unit
}
