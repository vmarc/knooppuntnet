package kpn.core.tools.country

import kpn.api.common.Country
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.tools.config.Dirs
import org.apache.commons.io.FileUtils
import org.xml.sax.SAXParseException

import java.io.File
import scala.xml.XML

class CountryBoundaryLoader {

  private val executor = new OverpassQueryExecutorImpl()

  def countryId(country: Country): Long = {
    val query = s"relation['admin_level'='2']['type'='boundary']['ISO3166-1'='${country.toString.toUpperCase}'];out ids;"
    val response = executor.execute(query)
    val xml = try {
      XML.loadString(response)
    }
    catch {
      case e: SAXParseException => throw new RuntimeException(s"Could not load boundary for country ${country.toString}", e)
    }
    (xml.head \ "relation").map(a => a \ "@id").map(_.text.toLong).filterNot(_ == 1111111).head
  }

  def countryData(country: Country, countryId: Long): SkeletonData = {
    val response = executor.execute(s"relation($countryId);>>;out skel;")
    val filename = s"${Dirs.root}/country/debug/${country.toString}.xml"
    FileUtils.writeStringToFile(new File(filename), response, "UTF-8")
    val xml = XML.loadString(response)
    new SkeletonParser().parse(xml.head).copy(countryRelationId = countryId)
  }
}
