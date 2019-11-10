package kpn.server.analyzer.engine.analysis.country

import java.io._

import kpn.api.custom.Country
import org.locationtech.jts.io.WKTReader

object CountryBoundaryReader {

  def read(country: Country): CountryBoundary = {

    val filter = new FilenameFilter() {
      def accept(dir: File, name: String): Boolean = {
        name.contains(country.domain) && name.endsWith(".poly")
      }
    }

    val files = new File("/kpn/country").listFiles(filter).toSeq.sorted

    val reader = new WKTReader()
    val polygons = files.map { file =>
      val fileReader = new FileReader(file)
      try {
        reader.read(fileReader)
      }
      finally {
        fileReader.close()
      }
    }
    new CountryBoundary(polygons)
  }
}
