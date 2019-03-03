package kpn.core.facade.pages

import kpn.core.directions.DirectionsEngine
import kpn.shared.directions.Directions
import kpn.shared.directions.DirectionsInstruction
import kpn.shared.directions.DirectionsPath

import scala.io.Source

case class Leg(startNode: String, endNode: String, gpxFilename: String)

object DirectionsBuilderImpl {

  private val examples: Map[String, Seq[Leg]] = Map(
    "example-1" -> Seq(
      Leg("32","08", "example-1-a-32-08"),
      Leg("08","93", "example-1-b-08-93"),
      Leg("93","92", "example-1-c-93-92"),
      Leg("92","11", "example-1-d-92-11"),
      Leg("11","91", "example-1-e-11-91"),
      Leg("91","34", "example-1-f-91-34"),
      Leg("34","35", "example-1-g-34-35")
    )
  )

}

class DirectionsBuilderImpl(directionsEngine: DirectionsEngine) extends DirectionsBuilder {

  override def build(language: String, exampleName: String): Option[Directions] = {

    DirectionsBuilderImpl.examples.get(exampleName) match {
      case Some(legs) =>

        val directionsCollection: Seq[Directions] = legs.flatMap { leg =>

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

        val paths: Seq[DirectionsPath] = directionsCollection.flatMap(_.paths.toSeq).flatten

        val distance = paths.map(_.distance).sum
        val ascend = paths.map(_.ascend).sum
        val descend = paths.map(_.descend).sum

        val firstInstruction = DirectionsInstruction(
          text = Some(legs.head.startNode),
          sign = Some(99)
        )

        val instructions: Seq[DirectionsInstruction] = paths.zip(legs).flatMap { case(path, leg) =>
          val lastInstruction = DirectionsInstruction(
            text = Some(leg.endNode),
            sign = Some(99)
          )
          path.instructions.dropRight(1) :+ lastInstruction
        }

        val allInstructions: Seq[DirectionsInstruction] = firstInstruction +: instructions

        val directionsPath = DirectionsPath(
          distance = distance,
          ascend = ascend,
          descend = descend,
          instructions = allInstructions
        )

        Some(
          Directions(
            Some(
              Seq(
                directionsPath
              )
            )
          )
        )

      case _ => None
    }
  }
}
