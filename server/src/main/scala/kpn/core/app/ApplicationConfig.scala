package kpn.core.app

import kpn.core.db.couch.CouchConfig

trait ApplicationConfig {
  def couchConfig: CouchConfig
}
