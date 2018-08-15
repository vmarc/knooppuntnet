package kpn.shared.route

import kpn.shared.LatLon

case class GeometryDiff(
  common: Seq[PointSegment] = Seq(), // blue
  before: Seq[PointSegment] = Seq(), // red
  after: Seq[PointSegment] = Seq() // green
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
