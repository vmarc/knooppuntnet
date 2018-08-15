package kpn.core.repository

import kpn.core.db.BlackListDoc
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.blackListDocFormat
import kpn.core.engine.changes.data.BlackList

class BlackListRepositoryImpl(database: Database) extends BlackListRepository {

  private val docId = "black-list"
  private val CACHE_TIMEOUT_MILLIS = 30000

  private var cachedBlackList: Option[BlackList] = None
  private var cachedTimestamp: Option[Long] = None

  def get: BlackList = {
    val now = System.currentTimeMillis()
    if (cachedTimestamp.isEmpty || cachedTimestamp.get < (now - CACHE_TIMEOUT_MILLIS)) {
      val blackListDoc = blackListDocFormat.read(database.getJsValue(docId))
      cachedBlackList = Some(blackListDoc.blackList)
      cachedTimestamp = Some(now)
      blackListDoc.blackList
    }
    else {
      cachedBlackList.get
    }
  }

  def save(blackList: BlackList): Unit = {
    val rev = database.currentRevision(docId)
    database.save(docId, blackListDocFormat.write(BlackListDoc(docId, blackList, rev)))
  }
}
