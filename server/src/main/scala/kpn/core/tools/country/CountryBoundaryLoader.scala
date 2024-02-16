package kpn.core.tools.country

import java.io.File

import kpn.api.custom.Country
import kpn.core.overpass.OverpassQueryExecutorImpl
import org.apache.commons.io.FileUtils
import org.xml.sax.SAXParseException

import scala.xml.XML

class CountryBoundaryLoader {

  private val executor = new OverpassQueryExecutorImpl()

  def countryId(country: Country): Long = {
    val query = s"relation['admin_level'='2']['type'='boundary']['ISO3166-1'='${country.domain.toUpperCase}'];out ids;"
    val response = executor.execute(query)
    val xml = try {
      XML.loadString(response)
    }
    catch {
      case e: SAXParseException => throw new RuntimeException(s"Could not load boundary for country ${country.domain}", e)
    }
    (xml.head \ "relation").map(a => a \ "@id").map(_.text.toLong).filterNot(_ == 1111111).head
  }

  def countryData(country: Country, countryId: Long): SkeletonData = {
    val response = executor.execute(s"relation($countryId);>>;out skel;")
    FileUtils.writeStringToFile(new File(s"/kpn/country/debug/${country.domain}.xml"), response, "UTF-8")
    val xml = XML.loadString(response)
    new SkeletonParser().parse(xml.head).copy(countryRelationId = countryId)
  }
}
