package kpn.core.database

import kpn.core.database.query.Query

trait Database {

  def execute[T](query: Query[T]): T

}
