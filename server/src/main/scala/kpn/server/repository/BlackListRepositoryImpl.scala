package kpn.server.repository

import kpn.core.database.Database
import kpn.core.database.doc.BlackListDoc
import kpn.server.analyzer.engine.changes.data.BlackList
import org.springframework.stereotype.Component

@Component
class BlackListRepositoryImpl(analysisDatabase: Database) extends BlackListRepository {

  private val docId = "black-list"
  private val CACHE_TIMEOUT_MILLIS = 30000

  private var cachedBlackList: Option[BlackList] = None
  private var cachedTimestamp: Option[Long] = None

  def get: BlackList = {
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

  def save(blackList: BlackList): Unit = {
    val rev = analysisDatabase.revision(docId)
    analysisDatabase.save(BlackListDoc(docId, blackList, rev))
  }
}
