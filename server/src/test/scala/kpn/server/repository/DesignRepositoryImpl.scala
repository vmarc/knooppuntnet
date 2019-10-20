package kpn.server.repository

import kpn.core.db.couch.Database
import kpn.core.db.couch.DesignDoc
import kpn.core.db.couch.ViewDoc
import kpn.core.db.views.Design
import kpn.core.util.Util

class DesignRepositoryImpl(database: Database) extends DesignRepository {

  def save(design: Design): Unit = {
    val views = design.views.map(v => v.name -> ViewDoc(v.map, v.reduce)).toMap
    val id = "_design/" + Util.classNameOf(design)
    val rev = database.revision(id)
    val designDoc = DesignDoc(id, rev, "javascript", views)
    database.save(designDoc)
  }
}
