package kpn.server.repository

import kpn.server.analyzer.engine.changes.data.Blacklist

trait BlacklistRepository {

  def get(now: Long = System.currentTimeMillis()): Blacklist

  def save(blacklist: Blacklist, now: Long = System.currentTimeMillis()): Unit
}
