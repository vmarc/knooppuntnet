package kpn.shared.route

import kpn.shared.LatLon

case class GeometryDiff(
  common: Seq[PointSegment]= Seq.empty, // blue
  before: Seq[PointSegment]= Seq.empty, // red
  after: Seq[PointSegment]= Seq.empty // green
) {

  def updatedLatLons: Seq[LatLon] = {
    (latLonsIn(before) ++ latLonsIn(after)).distinct
  }

  def latLons: Seq[LatLon] = {
    (latLonsIn(common) ++ latLonsIn(before) ++ latLonsIn(after)).distinct
  }

  private def latLonsIn(segments: Seq[PointSegment]): Seq[LatLon] = {
    (segments.map(_.p1) ++ segments.map(_.p2)).distinct
  }
}
