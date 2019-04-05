package kpn.core.facade.pages.directions

import kpn.core.db.json.JsonFormatsDirections
import kpn.core.directions.DirectionsEngine
import kpn.core.util.Log
import kpn.shared.directions.Directions
import kpn.shared.directions.DirectionsInstruction
import spray.json.JsonParser

import scala.io.Source

object DirectionsBuilderImpl {

  private val examples: Map[String, Seq[Leg]] = Map(
    "example-1" -> Seq(
      Leg("32", "08", "example-1-a-32-08"),
      Leg("08", "93", "example-1-b-08-93"),
      Leg("93", "92", "example-1-c-93-92"),
      Leg("92", "11", "example-1-d-92-11"),
      Leg("11", "91", "example-1-e-11-91"),
      Leg("91", "34", "example-1-f-91-34"),
      Leg("34", "35", "example-1-g-34-35")
    )
  )

}

class DirectionsBuilderImpl(directionsEngine: DirectionsEngine) extends DirectionsBuilder {

  private val log = Log(classOf[DirectionsBuilderImpl])

  override def build(language: String, exampleName: String): Option[Directions] = {
    DirectionsBuilderImpl.examples.get(exampleName) match {
      case Some(legs) => Some(toDirections(legs, legs.flatMap(fetchDirectionResults)))
      case _ => None
    }
  }

  private def fetchDirectionResults(leg: Leg): Option[GraphHopperDirections] = {

    val filename = "/directions-examples/" + leg.gpxFilename + ".json"
    val resource = getClass.getResourceAsStream(filename)
    if (resource == null) {
      log.error(s"Could not find resource with name $filename")
      None
    }
    else {
      try {
        val string = Source.fromInputStream(resource).getLines().mkString
        val json = JsonParser(string)
        Some(JsonFormatsDirections.graphHopperDirectionsFormat.read(json))
      }
      catch {
        case e: Exception =>
          e.printStackTrace()
          log.error(s"Could not interprete $filename", e)
          None
      }
    }
  }

  private def fetchDirections(leg: Leg): Option[GraphHopperDirections] = {

    val filename = "/directions-examples/" + leg.gpxFilename + ".gpx"
    val resource = getClass.getResourceAsStream(filename)
    if (resource == null) {
      log.error(s"Could not find resource with name $filename")
      None
    }
    else {
      try {
        val string = Source.fromInputStream(resource).getLines().mkString
        val res = directionsEngine.get("nl", string)
        res
      }
      catch {
        case e: Exception =>
          e.printStackTrace()
          log.error(s"Could not interprete $filename", e)
          None
      }
    }
  }


  private def toDirections(legs: Seq[Leg], graphHopperDirections: Seq[GraphHopperDirections]): Directions = {

    val paths: Seq[GraphHopperDirectionsPath] = graphHopperDirections.flatMap(_.paths.toSeq).flatten

    val distance = Math.round(paths.map(_.distance).sum).toInt
    val ascend = Math.round(paths.map(_.ascend).sum).toInt
    val descend = Math.round(paths.map(_.descend).sum).toInt

    val firstInstruction = DirectionsInstruction(node = Some(legs.head.startNode))

    val instructions: Seq[DirectionsInstruction] = paths.zip(legs).flatMap { case (path, leg) =>
      val lastInstruction = DirectionsInstruction(node = Some(leg.endNode))

      val legInstructions = path.instructions.map { graphHopperInstruction =>
        val distance = graphHopperInstruction.distance.map(d => Math.round(d).toInt)
        val sign = graphHopperInstruction.sign match {
          case Some(-7) => Some("keep-left")
          case Some(-3) => Some("turn-sharp-left")
          case Some(-2) => Some("turn-left")
          case Some(-1) => Some("turn-slight-left")
          case Some(0) => Some("continue")
          case Some(1) => Some("turn-slight-right")
          case Some(2) => Some("turn-right")
          case Some(3) => Some("turn-sharp-right")
          case Some(4) => Some("finish")
          case Some(5) => Some("via")
          case Some(6) => Some("roundabout")
          case Some(7) => Some("keep-right")
          case _ => None
        }
        DirectionsInstruction(
          text = graphHopperInstruction.text,
          streetName = graphHopperInstruction.streetName,
          distance = distance,
          sign = sign,
          annotationText = graphHopperInstruction.annotationText,
          annotationImportance = graphHopperInstruction.annotationImportance,
          exitNumber = graphHopperInstruction.exitNumber,
          turnAngle = graphHopperInstruction.turnAngle
        )
      }
      legInstructions.dropRight(1) :+ lastInstruction
    }

    val allInstructions: Seq[DirectionsInstruction] = firstInstruction +: instructions

    Directions(
      distance,
      ascend,
      descend,
      allInstructions
    )
  }

}
