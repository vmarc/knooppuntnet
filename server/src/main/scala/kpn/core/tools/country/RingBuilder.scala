package kpn.core.tools.country

import scala.annotation.tailrec
import scala.collection.mutable

object RingBuilder {

  def findRings(ways: Seq[SkeletonWay]): Seq[Ring] = {
    findRings(ways, Seq.empty)
  }

  private def findRings(ways: Seq[SkeletonWay], rings: Seq[Ring]): Seq[Ring] = {
    if (ways.isEmpty) {
      rings
    } else {
      findRings(ways.tail, Ring(ways.head), rings)
    }
  }

  @tailrec
  private def findRings(ways: Seq[SkeletonWay], currentRing: Ring, rings: Seq[Ring]): Seq[Ring] = {

    val start = currentRing.startNodeId
    val connectingWay = currentRing.ways.last
    val connectingNode = connectingWay.nodeIds.last

    if (start == connectingNode) {
      findRings(ways, rings :+ currentRing)
    }
    else {
      val connectingWays = ways.flatMap { w =>
        if (w.nodeIds.head == connectingNode) {
          Some(w)
        } else if(w.nodeIds.last == connectingNode) {
          Some(SkeletonWay(w.id, w.nodeIds.reverse))
        }
        else {
          None
        }
      }
      if (connectingWays.isEmpty) {
        throw new RuntimeException(s"no connecting ways found. rings=${rings.size}, currentRing=${currentRing.size}" )
      }
      else if (connectingWays.length > 1) {
        val b = new mutable.StringBuilder
        b.append("multiple connecting ways found; connecting way ")
        b.append(connectingWay.id)
        b.append(" with end node ")
        b.append(connectingNode)
        b.append(" to ways ")
        b.append(connectingWays.map(_.id).mkString(", "))
        throw new RuntimeException(b.toString)
      }
      else {
        val w = connectingWays.head
        val remainingWays = ways.filterNot(_.id == w.id)

        if (w.nodeIds.last == start) {
          if (remainingWays.isEmpty) {
            rings :+ currentRing.withWay(w)
          }
          else {
            findRings(remainingWays.tail, Ring(remainingWays.head), rings :+ currentRing.withWay(w))
          }
        }
        else {
          findRings(remainingWays, currentRing.withWay(w), rings)
        }
      }
    }
  }
}
