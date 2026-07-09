package kpn.server.analyzer.engine.analysis.location

import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.tools.location.RelationPolygonBuilder
import org.bson.RawBsonDocument
import org.bson.conversions.Bson
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonWriter

import scala.xml.InputSource
import scala.xml.XML

// 83 communes https://www.parc-du-vercors.fr/le-perimetre-et-les-chiffres-cles
object ParcDuVercors {

  val name: String = "Parc du Vercors"

  val relationId = 5555268L

  val communes: Seq[String] = Seq(
    "fr-3-38018", // Auberives-en-Royans
    "fr-3-38225", // Autrans-Méaudre en Vercors
    "fr-3-26035", // Beaufort-sur-Gervanne
    "fr-3-38036", // Beauvoir-en-Royans
    "fr-3-26059", // Bouvante
    "fr-3-26069", // Chamaloc
    "fr-3-38090", // Château-Bernard
    "fr-3-38092", // Châtelus
    "fr-3-26086", // Châtillon-en-Diois
    "fr-3-38103", // Chichilianne
    "fr-3-38108", // Choranche
    "fr-3-38111", // Claix  *
    "fr-3-38113", // Clelles
    "fr-3-38117", // Cognin-les-Gorges
    "fr-3-26100", // Combovin
    "fr-3-38129", // Corrençon-en-Vercors
    "fr-3-26113", // Die
    "fr-3-26117", // Échevis
    "fr-3-38153", // Engins
    "fr-3-38169", // Fontaine  *
    "fr-3-26141", // Gigors-et-Lozeron
    "fr-3-26142", // Glandage
    "fr-3-38186", // Gresse-en-Vercors
    "fr-3-38195", // Izeron
    "fr-3-26074", // La Chapelle-en-Vercors
    "fr-3-26217", // La Motte-Fanjas
    "fr-3-38338", // La Rivière
    "fr-3-38205", // Lans-en-Vercors
    "fr-3-26159", // Laval-d'Aix
    "fr-3-26066", // Le Chaffal
    "fr-3-38187", // Le Gua
    "fr-3-26163", // Léoncel
    "fr-3-26168", // Lus-la-Croix-Haute
    "fr-3-38216", // Malleval-en-Vercors
    "fr-3-26175", // Marignac-en-Diois
    "fr-3-38235", // Miribel-Lanchâtre
    "fr-3-38243", // Le Monestier-du-Percy
    "fr-3-38301", // Percy
    "fr-3-38248", // Montaud
    "fr-3-38281", // Noyarey  *
    "fr-3-26221", // Omblèze
    "fr-3-26223", // Oriol-en-Royans
    "fr-3-26240", // Plan-de-Baix
    "fr-3-26246", // Ponet-et-Saint-Auban
    "fr-3-38319", // Pont-en-Royans
    "fr-3-38322", // Presles
    "fr-3-38333", // Rencurel
    "fr-3-26270", // Rochechinard
    "fr-3-26282", // Romeyer
    "fr-3-38345", // Rovon
    "fr-3-38474", // Sassenage  *
    "fr-3-38485", // Seyssinet-Pariset  *
    "fr-3-38486", // Seyssins  *
    "fr-3-26290", // Saint-Agnan-en-Vercors
    "fr-3-38355", // Saint-Andéol   Isère
    "fr-3-26291", // Saint-Andéol   Drôme
    "fr-3-38356", // Saint-André-en-Royans
    "fr-3-38390", // Saint-Gervais
    "fr-3-38391", // Saint-Guillaume
    "fr-3-26307", // Saint-Jean-en-Royans
    "fr-3-26308", // Saint-Julien-en-Quint
    "fr-3-26309", // Saint-Julien-en-Vercors
    "fr-3-38409", // Saint-Just-de-Claix
    "fr-3-26311", // Saint-Laurent-en-Royans
    "fr-3-38419", // Saint-Martin-de-Clelles
    "fr-3-26315", // Saint-Martin-en-Vercors
    "fr-3-26316", // Saint-Martin-le-Colonel
    "fr-3-38429", // Saint-Michel-les-Portes
    "fr-3-26320", // Saint-Nazaire-en-Royans
    "fr-3-38433", // Saint-Nizier-du-Moucherotte
    "fr-3-38438", // Saint-Paul-lès-Monestier
    "fr-3-38443", // Saint-Pierre-de-Chérennes
    "fr-3-38450", // Saint-Quentin-sur-Isère  *
    "fr-3-38453", // Saint-Romans
    "fr-3-26331", // Saint-Thomas-en-Royans
    "fr-3-38436", // Saint-Paul-de-Varces
    "fr-3-26299", // Sainte-Croix
    "fr-3-26302", // Sainte-Eulalie-en-Royans
    "fr-3-26359", // Vachères-en-Quint
    "fr-3-38524", // Varces-Allières-et-Risset  *
    "fr-3-26364", // Vassieux-en-Vercors
    "fr-3-38540", // Veurey-Voroize  *
    "fr-3-38548", // Villard-de-Lans
  )

  val partialCommunes: Seq[String] = Seq(
    "fr-3-38111", // Claix  *
    "fr-3-38169", // Fontaine  *
    "fr-3-38281", // Noyarey  *
    "fr-3-38474", // Sassenage  *
    "fr-3-38485", // Seyssinet-Pariset  *
    "fr-3-38486", // Seyssins  *
    "fr-3-38450", // Saint-Quentin-sur-Isère  *
    "fr-3-38524", // Varces-Allières-et-Risset  *
    "fr-3-38540", // Veurey-Voroize  *
  )

  val boundaryGeometry: Geometry = {
    val filename = s"/kpn/locations/parc-du-vercors.xml"
    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser(includeMetadata = false).parse(xml.head)
    val data = new DataBuilder(rawData).data
    val relation = data.relations(relationId)
    val polygons = RelationPolygonBuilder.toPolygons(data, relation)
    if (polygons.sizeIs != 1) {
      polygons.head
    }
    else {
      val geometryFactory = new GeometryFactory
      new GeometryCollection(polygons.toArray, geometryFactory)
    }
  }

  val boundaryBson: Bson = {
    val geoJsonWriter = new GeoJsonWriter()
    geoJsonWriter.setEncodeCRS(false)
    val s = geoJsonWriter.write(boundaryGeometry)
    RawBsonDocument.parse(s)
  }
}
