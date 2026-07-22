package kpn.server.analyzer.engine.tiles

case class TileFileSnapshot(
  vectorTileNames: Seq[String] = Seq.empty,
  bitmapTileNames: Seq[String] = Seq.empty,
  bitmapTileNamesSurface: Seq[String] = Seq.empty,
  bitmapTileNamesSurvey: Seq[String] = Seq.empty,
  bitmapTileNamesAnalysis: Seq[String] = Seq.empty,
)
