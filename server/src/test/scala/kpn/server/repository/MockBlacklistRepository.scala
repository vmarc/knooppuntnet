package kpn.server.repository

import kpn.server.analyzer.engine.changes.data.Blacklist

class MockBlacklistRepository extends BlacklistRepository {

  private var storedBlacklist = Blacklist()

  def get(now: Long): Blacklist = storedBlacklist

  def save(blacklist: Blacklist, now: Long): Unit = {
    storedBlacklist = blacklist
  }
}
