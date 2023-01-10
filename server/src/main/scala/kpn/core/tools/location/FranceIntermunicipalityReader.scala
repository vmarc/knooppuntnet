package kpn.core.tools.location

import kpn.api.custom.Relation
import kpn.core.data.DataBuilder
import kpn.core.doc.LocationNames
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.util.Log
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory

class FranceIntermunicipalityReader(intermunicipalitiesDir: String, locationId: Long, intermunicipalityType: String) {

  def read(): LocationData = {
    Log.context(s"intermunicipality $locationId") {
      val geometryFactory = new GeometryFactory
      val rawData = OsmDataXmlReader.read(s"$intermunicipalitiesDir/$locationId.xml")
      val data = new DataBuilder(rawData).data
      val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", intermunicipalityType))
      if (locationRelations.size != 1) {
        throw new RuntimeException(error(s"unexpected number of location relations: ${locationRelations.size}"))
      }
      val relation = loadIntermunicipalityRelation(locationId, intermunicipalityType)
      val name = relation.tags("name").getOrElse(throw new RuntimeException(error("name not found")))
      val sirenCode = relation.tags("ref:FR:SIREN").getOrElse(throw new RuntimeException(error("SIREN code not found")))
      val id = s"fr-2-$sirenCode"
      val polygons = RelationPolygonBuilder.toPolygons(data, relation)
      val geometry = if (polygons.size != 1) {
        polygons.head
      }
      else {
        new GeometryCollection(polygons.toArray, geometryFactory)
      }
      val names = LocationNames.from(relation.tags, name)
      LocationData(
        id,
        Seq.empty,
        name,
        names,
        LocationGeometry(geometry)
      )
    }
  }

  private def loadIntermunicipalityRelation(locationId: Long, intermunicipalityType: String): Relation = {
    val rawData = OsmDataXmlReader.read(s"$intermunicipalitiesDir/$locationId.xml")
    val data = new DataBuilder(rawData).data
    val locationRelations = data.relations.values.filter(_.tags.has("local_authority:FR", intermunicipalityType))
    if (locationRelations.size != 1) {
      throw new RuntimeException(error(s"unexpected number of location relations: ${locationRelations.size}"))
    }
    locationRelations.head
  }

  private def error(message: String): String = {
    s"Intermunicipality $locationId: $message"
  }
}
