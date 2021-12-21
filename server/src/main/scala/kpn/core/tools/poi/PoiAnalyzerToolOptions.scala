package kpn.core.tools.poi

object PoiAnalyzerToolOptions {

  def parse(args: Array[String]): Option[PoiAnalyzerToolOptions] = {
    optionParser.parse(args, PoiAnalyzerToolOptions())
  }

  private def optionParser: scopt.OptionParser[PoiAnalyzerToolOptions] = {
    new scopt.OptionParser[PoiAnalyzerToolOptions]("PoiTileAnalyzerTool") {
      head("PoiAnalyzerTool")
      opt[String]('p', "poi-database").required() valueName "<database-name>" action { (x, c) =>
        c.copy(poiDatabaseName = x)
      } text "poi database name"
    }
  }
}

case class PoiAnalyzerToolOptions(
  poiDatabaseName: String = ""
)
