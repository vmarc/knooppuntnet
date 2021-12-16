package kpn.core.tools.location

import scala.collection.mutable.ListBuffer

class LocationDatas {
  private val locationDatas = ListBuffer[LocationData]()

  def toSeq: Seq[LocationData] = {
    locationDatas.toSeq
  }

  def add(data: LocationData): Unit = {
    synchronized {
      locationDatas += data
    }
  }

  def startingWith(id: String): Seq[LocationData] = {
    toSeq.filter(_.id.startsWith(id))
  }
}
