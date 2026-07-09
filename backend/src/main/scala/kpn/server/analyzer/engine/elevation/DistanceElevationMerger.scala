package kpn.server.analyzer.engine.elevation

import scala.annotation.tailrec

object DistanceElevationMerger {

  def merge(des: Seq[DistanceElevation]): Seq[DistanceElevation] = {
    merge(des, None, 0, Seq.empty)
  }

  @tailrec
  private def merge(des: Seq[DistanceElevation], elevationOption: Option[Int], distance: Double, merged: Seq[DistanceElevation]): Seq[DistanceElevation] = {
    if (des.isEmpty) {
      elevationOption match {
        case Some(elevation) => merged :+ DistanceElevation(distance, elevation)
        case None => Seq.empty
      }
    }
    else {
      elevationOption match {
        case None => merge(des.tail, Some(des.head.elevation), des.head.distance, merged)
        case Some(elevation) =>
          if (des.head.elevation == elevation) {
            merge(des.tail, Some(elevation), distance + des.head.distance, merged)
          }
          else {
            merge(des.tail, Some(des.head.elevation), des.head.distance, merged :+ DistanceElevation(distance, elevation))
          }
      }
    }
  }

}
