package kpn.api.common

import kpn.api.common.common.Ref

case class NetworkFact(
  name: String,
  elementType: Option[String] = None,
  elementIds: Option[Seq[Long]] = None,
  elements: Option[Seq[Ref]] = None,
  checks: Option[Seq[Check]] = None
) {

  def size: Long = {
    elementIds match {
      case Some(ids) => ids.size
      case None =>
        elements match {
          case Some(es) => es.size
          case None =>
            checks match {
              case Some(c) => c.size
              case None => 0
            }
        }
    }
  }
}
