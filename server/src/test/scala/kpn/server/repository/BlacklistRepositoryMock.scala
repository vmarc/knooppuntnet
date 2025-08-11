package kpn.server.repository

import kpn.server.analyzer.engine.changes.data.Blacklist

class BlacklistRepositoryMock extends BlacklistRepository {

  private var storedBlacklist = Blacklist()

  def find(now: Long): Blacklist = storedBlacklist

  def save(blacklist: Blacklist, now: Long): Unit = {
    storedBlacklist = blacklist
  }
}
