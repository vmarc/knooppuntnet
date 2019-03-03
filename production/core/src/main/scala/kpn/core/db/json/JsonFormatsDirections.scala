package kpn.core.db.json

import kpn.shared.directions.Directions
import kpn.shared.directions.DirectionsInstruction
import kpn.shared.directions.DirectionsPath
import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat

object JsonFormatsDirections extends DefaultJsonProtocol {

  implicit val directionsInstructionFormat: RootJsonFormat[DirectionsInstruction] = jsonFormat10(DirectionsInstruction)
  implicit val directionsPathFormat: RootJsonFormat[DirectionsPath] = jsonFormat7(DirectionsPath)
  implicit val directionsFormat: RootJsonFormat[Directions] = jsonFormat1(Directions)

}
