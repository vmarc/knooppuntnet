package kpn.core.tools.location

import kpn.core.doc.LocationDoc
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.gte
import org.mongodb.scala.model.Filters.lt

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

object LocationTreePrinter {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      new LocationTreePrinter(database).print()
    }
  }
}

class LocationTreePrinter(database: Database) {


  private val fw = new FileWriter(new File("/kpn/locations/france.md"))
  private val out = new PrintWriter(fw)

  def print(): Unit = {
    val locations = database.locations.find[LocationDoc](
      and(
        gte("_id", "fr"),
        lt("_id", "fz")
      )
    )

    printLocation(locations, "fr", 0)
    out.close()
  }

  private def printLocation(locations: Seq[LocationDoc], id: String, indent: Int): Unit = {
    locations.find(_._id == id) match {
      case None => throw new RuntimeException(s"could not find $id")
      case Some(parent) =>
        val spaces = (0 to indent).map(x => " ").mkString
        val children = locations.filter(_.parents.contains(parent._id)).sortBy(_.name)
        val count = if (children.nonEmpty) s" (${children.size})" else ""
        out.println(s"$spaces- `${parent._id}` ${parent.name}$count")
        children.foreach { child =>
          printLocation(locations, child._id, indent + 2)
        }
    }
  }
}

