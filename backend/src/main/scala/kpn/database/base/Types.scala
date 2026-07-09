package kpn.database.base

import org.bson.conversions.Bson

object Types {
  type MongoPipeline = Seq[Bson]
}
