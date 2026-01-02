package kpn.api.common.route

object GeometryDiff {

  def apply(
    common: Seq[WayGeometry],
    update: Seq[WayGeometryUpdate]
  ): GeometryDiff = {

    val commonDetail = GeometryDiffInfoDetail(
      wayCount = common.length + update.count(_.common.nonEmpty),
      nodeCount = common.map(_.line.nodeCount).sum + update.flatMap(_.common).flatten.map(_.nodeCount).sum,
      meters = common.map(_.line.meters).sum + update.flatMap(_.common).flatten.map(_.meters).sum,
    )

    val addedDetail = GeometryDiffInfoDetail(
      wayCount = update.count(_.added.nonEmpty),
      nodeCount = update.flatMap(_.added).flatten.map(_.nodeCount).sum,
      meters = update.flatMap(_.added).flatten.map(_.meters).sum,
    )

    val removedDetail = GeometryDiffInfoDetail(
      wayCount = update.count(_.removed.nonEmpty),
      nodeCount = update.flatMap(_.removed).flatten.map(_.nodeCount).sum,
      meters = update.flatMap(_.removed).flatten.map(_.meters).sum,
    )

    val info = GeometryDiffInfo(
      common = commonDetail,
      added = addedDetail,
      removed = removedDetail,
    )

    GeometryDiff(
      info,
      common,
      update
    )
  }
}

case class GeometryDiff(
  info: GeometryDiffInfo,
  common: Seq[WayGeometry],
  update: Seq[WayGeometryUpdate]
)
