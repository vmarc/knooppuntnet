package kpn.core.facade.pages.directions

import kpn.core.directions.DirectionsEngine
import kpn.shared.directions.Directions
import kpn.shared.directions.DirectionsInstruction

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

  override def build(language: String, exampleName: String): Option[Directions] = {
    DirectionsBuilderImpl.examples.get(exampleName) match {
      case Some(legs) => Some(toDirections(legs, legs.flatMap(fetchDirections)))
      case _ => None
    }
  }

  private def fetchDirections(leg: Leg): Option[GraphHopperDirections] = {

    val filename = "/directions-examples/" + leg.gpxFilename + ".gpx"
    println("FILENAME=" + filename)

    val resource = getClass.getResourceAsStream(filename)
    if (resource == null) {
      println("ERROR: Could not find resource with name " + filename)
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
          println("ERROR:" + e.getMessage)
          None
      }
    }
  }


  private def toDirections(legs: Seq[Leg], graphHopperDirections: Seq[GraphHopperDirections]): Directions = {

    val paths: Seq[GraphHopperDirectionsPath] = graphHopperDirections.flatMap(_.paths.toSeq).flatten

    val distance = Math.round(paths.map(_.distance).sum).toInt
    val ascend = Math.round(paths.map(_.ascend).sum).toInt
    val descend = Math.round(paths.map(_.descend).sum).toInt

    val firstInstruction = DirectionsInstruction(
      text = Some(legs.head.startNode),
      sign = Some(99)
    )

    val instructions: Seq[DirectionsInstruction] = paths.zip(legs).flatMap { case (path, leg) =>
      val lastInstruction = DirectionsInstruction(
        text = Some(leg.endNode),
        sign = Some(99)
      )

      val legInstructions= path.instructions.map { graphHopperInstruction =>
        val distance = graphHopperInstruction.distance.map(d => Math.round(d).toInt)
        DirectionsInstruction(
          text = graphHopperInstruction.text,
          streetName = graphHopperInstruction.streetName,
          distance = distance,
          sign = graphHopperInstruction.sign,
          annotationText = graphHopperInstruction.annotationText,
          annotationImportance = graphHopperInstruction.annotationImportance,
          exitNumber = graphHopperInstruction.exitNumber,
          turnAngle = graphHopperInstruction.turnAngle
        )
      }
      legInstructions.dropRight(1)  :+ lastInstruction
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
