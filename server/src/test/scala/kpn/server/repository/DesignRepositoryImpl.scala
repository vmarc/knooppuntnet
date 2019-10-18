package kpn.server.repository

import kpn.core.db.couch.Couch
import kpn.core.db.couch.OldDatabase
import kpn.core.db.couch.DesignDoc
import kpn.core.db.couch.ViewDoc
import kpn.core.db.json.JsonFormats.designDocFormat
import kpn.core.db.views.Design
import kpn.core.util.Util

class DesignRepositoryImpl(database: OldDatabase) extends DesignRepository {

  def save(design: Design): Unit = {
    val views = design.views.map(v => v.name -> ViewDoc(v.map, v.reduce)).toMap
    val id = "_design/" + Util.classNameOf(design)
    val rev = database.currentRevision(id, Couch.batchTimeout)
    val designDoc = DesignDoc(id, rev, "javascript", views)
    database.authorizedSsave(id, designDocFormat.write(designDoc))
  }
}
