package kpn.core.tools.poi

object PoiTileAnalyzerToolOptions {

  def parse(args: Array[String]): Option[PoiTileAnalyzerToolOptions] = {
    optionParser.parse(args, PoiTileAnalyzerToolOptions())
  }

  private def optionParser: scopt.OptionParser[PoiTileAnalyzerToolOptions] = {
    new scopt.OptionParser[PoiTileAnalyzerToolOptions]("PoiTileAnalyzerTool") {
      head("PoiTileAnalyzerTool")
      opt[String]('h', "host").required() valueName "<host>" action { (x, c) =>
        c.copy(host = x)
      } text "database host name"
      opt[String]('p', "poi-database").required() valueName "<database-name>" action { (x, c) =>
        c.copy(poiDatabaseName = x)
      } text "poi database name"
    }
  }
}

case class PoiTileAnalyzerToolOptions(
  host: String = "",
  poiDatabaseName: String = ""
)
