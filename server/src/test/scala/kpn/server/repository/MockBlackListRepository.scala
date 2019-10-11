package kpn.server.repository

import kpn.core.engine.changes.data.BlackList

class MockBlackListRepository extends BlackListRepository {

  private var storedBlackList = BlackList()

  def get: BlackList = storedBlackList

  def save(blackList: BlackList): Unit = {
    storedBlackList = blackList
  }
}
