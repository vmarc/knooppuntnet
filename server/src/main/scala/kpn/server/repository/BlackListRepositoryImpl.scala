package kpn.server.repository

import kpn.core.database.doc.BlackListDoc
import kpn.core.mongo.Database
import kpn.server.analyzer.engine.changes.data.BlackList
import org.springframework.stereotype.Component

@Component
class BlackListRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends BlackListRepository {

  private val docId = "black-list"
  private val CACHE_TIMEOUT_MILLIS = 30000

  private var cachedBlackList: Option[BlackList] = None
  private var cachedTimestamp: Option[Long] = None

  def get: BlackList = {
    if (mongoEnabled) {
      BlackList() // TODO MONGO implement
    }
    else {
      val now = System.currentTimeMillis()
      if (cachedTimestamp.isEmpty || cachedTimestamp.get < (now - CACHE_TIMEOUT_MILLIS)) {
        val blackListDoc = analysisDatabase.docWithId(docId, classOf[BlackListDoc])
        cachedBlackList = blackListDoc.map(_.blackList)
        cachedTimestamp = Some(now)
        blackListDoc.get.blackList
      }
      else {
        cachedBlackList.get
      }
    }
  }

  def save(blackList: BlackList): Unit = {
    if (mongoEnabled) {
      ???
    }
    else {
      analysisDatabase.save(BlackListDoc(docId, blackList))
    }
  }
}
