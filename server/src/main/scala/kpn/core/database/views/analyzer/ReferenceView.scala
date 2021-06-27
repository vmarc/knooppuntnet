package kpn.core.database.views.analyzer

import kpn.api.common.common.Reference
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.query.Fields
import kpn.core.database.query.Query
import kpn.core.database.views.common.View

object ReferenceView extends View {

  case class Row(
    referencedType: String,
    referencedId: Long,
    referrerType: String,
    referrerId: Long,
    referrerNetworkType: NetworkType,
    referrerNetworkScope: NetworkScope,
    referrerName: String
  ) {
    def toReference: Reference = {
      Reference(
        referrerNetworkType,
        referrerNetworkScope,
        referrerId,
        referrerName
      )
    }
  }

  private case class ViewResultRow(
    id: String,
    key: Seq[String],
    value: Seq[String]
  )

  private case class ViewResult(rows: Seq[ViewResultRow])

  def query(database: Database, referencedType: String, referencedId: Long, stale: Boolean = true): Seq[Row] = {
    val query = Query(AnalyzerDesign, ReferenceView, classOf[ViewResult])
      .keyStartsWith(referencedType, referencedId)
      .reduce(false)
      .stale(stale)
    val result = database.execute(query)
    result.rows.map { row =>
      val key = Fields(row.key)
      val value = Fields(row.value)
      Row(
        referencedType = key.string(0),
        referencedId = key.long(1),
        referrerType = key.string(2),
        referrerId = key.long(3),
        referrerNetworkType = value.networkType(0),
        referrerNetworkScope = value.networkScope(1),
        referrerName = value.string(2)
      )
    }
  }

  override val reduce: Option[String] = None
}
