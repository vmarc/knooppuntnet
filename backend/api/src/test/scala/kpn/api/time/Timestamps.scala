package kpn.api.time

import kpn.api.custom.Timestamp

object Timestamps {
  val default: Timestamp = Timestamp(2015, 8, 11, 0, 0, 0)
  val before: Timestamp = Timestamp(2015, 8, 11, 0, 0, 1)
  val from: Timestamp = Timestamp(2015, 8, 11, 0, 0, 2)
  val until: Timestamp = Timestamp(2015, 8, 11, 0, 0, 3)
  val after: Timestamp = Timestamp(2015, 8, 11, 0, 0, 4)
}
