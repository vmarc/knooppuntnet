package kpn.core.database

import kpn.core.database.implementation.DatabaseContext
import kpn.core.database.implementation.DatabaseQuery
import kpn.core.database.query.Query

class DatabaseImpl(context: DatabaseContext) extends Database {

  override def execute[T](query: Query[T]): T = {
    new DatabaseQuery(context).execute(query)
  }

}
