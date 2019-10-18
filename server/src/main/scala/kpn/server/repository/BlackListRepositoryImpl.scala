package kpn.server.repository

import kpn.core.db.BlackListDoc
import kpn.core.db.couch.OldDatabase
import kpn.core.db.json.JsonFormats.blackListDocFormat
import kpn.server.analyzer.engine.changes.data.BlackList
import org.springframework.stereotype.Component

@Component
class BlackListRepositoryImpl(oldAnalysisDatabase: OldDatabase) extends BlackListRepository {

  private val docId = "black-list"
  private val CACHE_TIMEOUT_MILLIS = 30000

  private var cachedBlackList: Option[BlackList] = None
  private var cachedTimestamp: Option[Long] = None

  def get: BlackList = {
    val now = System.currentTimeMillis()
    if (cachedTimestamp.isEmpty || cachedTimestamp.get < (now - CACHE_TIMEOUT_MILLIS)) {
      val blackListDoc = blackListDocFormat.read(oldAnalysisDatabase.getJsValue(docId))
      cachedBlackList = Some(blackListDoc.blackList)
      cachedTimestamp = Some(now)
      blackListDoc.blackList
    }
    else {
      cachedBlackList.get
    }
  }

  def save(blackList: BlackList): Unit = {
    val rev = oldAnalysisDatabase.currentRevision(docId)
    oldAnalysisDatabase.save(docId, blackListDocFormat.write(BlackListDoc(docId, blackList, rev)))
  }
}
