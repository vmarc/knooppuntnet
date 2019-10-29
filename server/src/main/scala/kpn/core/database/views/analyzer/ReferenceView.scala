package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.query.Query
import kpn.core.database.views.common.View
import kpn.shared.NetworkType
import kpn.shared.common.Reference

object ReferenceView extends View {

  case class Row(
    referencedType: String,
    referencedId: Long,
    referrerType: String,
    referrerId: Long,
    referrerNetworkType: NetworkType,
    referrerName: String,
    connection: Boolean
  ) {
    def toReference: Reference = {
      Reference(
        referrerId,
        referrerName,
        referrerNetworkType,
        connection
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
    val query = Query(AnalyzerDesign, ReferenceView, classOf[ViewResult]).reduce(false).keyStartsWith(referencedType, referencedId)
    val result = database.execute(query)
    result.rows.map { row =>
      NetworkType.withName(row.value.head) match {
        case None =>
          throw new IllegalStateException("Invalid networkType")
        case Some(networkType) =>
          Row(
            referencedType = row.key.head,
            referencedId = row.key(1).toLong,
            referrerType = row.key(2),
            referrerId = row.key(3).toLong,
            referrerNetworkType = networkType,
            referrerName = row.value(1),
            connection = row.value(2).toBoolean
          )
      }
    }
  }

}
