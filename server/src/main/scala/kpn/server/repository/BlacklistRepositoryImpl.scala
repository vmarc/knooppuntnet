package kpn.server.repository

import kpn.database.base.Database
import kpn.server.analyzer.engine.changes.data.Blacklist
import org.springframework.stereotype.Component

@Component
class BlacklistRepositoryImpl(database: Database) extends BlacklistRepository {

  private val CACHE_TIMEOUT_MILLIS = 30000

  private var cachedBlackList: Option[Blacklist] = None
  private var cachedTimestamp: Option[Long] = None

  def get(now: Long): Blacklist = {
    if (cachedTimestamp.isEmpty || cachedTimestamp.get < (now - CACHE_TIMEOUT_MILLIS)) {
      val blacklist = database.blacklists.findByStringId(Blacklist.id).getOrElse(Blacklist())
      cachedBlackList = Some(blacklist)
      cachedTimestamp = Some(now)
      blacklist
    }
    else {
      cachedBlackList.get
    }
  }

  def save(blacklist: Blacklist, now: Long): Unit = {
    database.blacklists.save(blacklist)
    cachedBlackList = Some(blacklist)
    cachedTimestamp = Some(now)
    ()
  }
}
