package kpn.server.repository

import kpn.server.analyzer.engine.changes.data.BlackList

trait BlackListRepository {

  def get: BlackList

  def save(blackList: BlackList): Unit
}
