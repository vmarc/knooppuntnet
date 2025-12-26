package kpn.server.repository

import kpn.server.analyzer.engine.changes.data.Blacklist

class BlacklistRepositoryMock extends BlacklistRepository {

  private var storedBlacklist = Blacklist()

  override def get(now: Long): Blacklist = storedBlacklist

  override def save(blacklist: Blacklist, now: Long): Unit = {
    storedBlacklist = blacklist
  }
}
