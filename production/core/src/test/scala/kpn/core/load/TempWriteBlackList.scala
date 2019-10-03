package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.engine.changes.data.BlackList
import kpn.core.repository.BlackListRepositoryImpl

object TempWriteBlackList extends App {

  val blackList = BlackList(
    Seq(),
    Seq(),
    Seq()
  )

  Couch.executeIn("master") { database =>
    val repository = new BlackListRepositoryImpl(database)
    repository.save(blackList)
  }
}
