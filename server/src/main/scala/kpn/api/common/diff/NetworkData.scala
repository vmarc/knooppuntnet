package kpn.api.common.diff

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawRelation

object NetworkData {
  def apply(metaData: MetaData, name: String): NetworkData = {
    NetworkData(None, Some(metaData), name)
  }
}

/*
  TODO MONGO after migration, change to:

    case class NetworkData(
      metaData: MetaData,
      name: String
    )
 */
case class NetworkData(
  relation: Option[RawRelation],
  metaData: Option[MetaData],
  name: String
) {

  def meta: MetaData = {
    metaData match {
      case Some(m) => m
      case None =>
        relation match {
          case None => throw new IllegalStateException("Cannot derive meta from: " + this)
          case Some(r) => r.toMeta
        }
    }
  }

  def migrated: NetworkData = {
    metaData match {
      case Some(metaData) =>
        relation match {
          case Some(relation) => copy(relation = None)
          case None => this
        }
      case None =>
        copy(
          relation = None,
          metaData = relation.map(_.toMeta)
        )
    }
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("relation", relation).
    field("metaData", metaData).
    field("name", name).
    build
}
