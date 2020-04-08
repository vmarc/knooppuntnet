package kpn.core.database.views.location

import kpn.api.common.common.Ref
import kpn.api.common.location.LocationFact
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object LocationFactView extends View {

  private case class ElementRef(elementType: String, ref: Ref)

  private case class ViewResult(
    rows: Seq[ViewResultRow]
  )

  private case class ViewResultRow(
    key: Seq[String],
    value: Long
  )

  def query(database: Database, networkType: NetworkType, locationName: String, stale: Boolean): Seq[LocationFact] = {

    val query = Query(LocationDesign, LocationFactView, classOf[ViewResult])
      .stale(stale)
      .keyStartsWith(networkType.name, locationName)
      .reduce(false)

    val result = database.execute(query)
    val factRefMap = result.rows.map { row =>
      val key = Fields(row.key)
      val elementType = key.string(2);
      val fact = key.string(3);
      val ref = Ref(key.long(5), key.string(4))
      fact -> ElementRef(elementType, ref)
    }.groupBy(_._1).map(kv => kv._1 -> kv._2.map(_._2))

    Fact.all.flatMap { fact =>
      factRefMap.get(fact.name).map { elementRefs =>
        LocationFact(
          elementRefs.head.elementType,
          fact,
          elementRefs.map(_.ref)
        )
      }
    }
  }

  def queryCount(database: Database, networkType: NetworkType, locationName: String, stale: Boolean): Long = {

    val query = Query(LocationDesign, LocationFactView, classOf[ViewResult])
      .stale(stale)
      .keyStartsWith(networkType.name, locationName)
      .groupLevel(2)
      .reduce(true)

    val result = database.execute(query)
    if (result.rows.nonEmpty) {
      result.rows.head.value
    }
    else {
      0
    }
  }

  override def reduce: Option[String] = Some("_count")
}
