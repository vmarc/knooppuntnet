package kpn.core.engine.analysis.country

import java.io._

import com.vividsolutions.jts.io.WKTReader
import kpn.shared.Country

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
