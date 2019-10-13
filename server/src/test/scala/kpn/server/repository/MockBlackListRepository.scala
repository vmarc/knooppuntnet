package kpn.server.repository

import kpn.server.analyzer.engine.changes.data.BlackList

class MockBlackListRepository extends BlackListRepository {

  private var storedBlackList = BlackList()

  def get: BlackList = storedBlackList

  def save(blackList: BlackList): Unit = {
    storedBlackList = blackList
  }
}
