package kpn.core.db.json

import kpn.core.facade.pages.directions.GraphHopperDirections
import kpn.core.facade.pages.directions.GraphHopperDirectionsInstruction
import kpn.core.facade.pages.directions.GraphHopperDirectionsPath
import kpn.shared.directions.Directions
import kpn.shared.directions.DirectionsInstruction
import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat

object JsonFormatsDirections extends DefaultJsonProtocol {

  implicit val directionsInstructionFormat: RootJsonFormat[DirectionsInstruction] = jsonFormat9(DirectionsInstruction)
  implicit val directionsFormat: RootJsonFormat[Directions] = jsonFormat4(Directions)

  implicit val graphHopperDirectionsInstructionFormat: RootJsonFormat[GraphHopperDirectionsInstruction] = jsonFormat8(GraphHopperDirectionsInstruction)
  implicit val graphHopperDirectionsPathFormat: RootJsonFormat[GraphHopperDirectionsPath] = jsonFormat4(GraphHopperDirectionsPath)
  implicit val graphHopperDirectionsFormat: RootJsonFormat[GraphHopperDirections] = jsonFormat1(GraphHopperDirections)

}
