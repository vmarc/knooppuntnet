// Migrated to Angular: interpreted-tags.ts
package kpn.client.common

import kpn.shared.data.Tag
import kpn.shared.data.Tagable
import kpn.shared.data.Tags

object NodeTagFilter {

  val standardTagKeys = Seq(
    "rwn_ref",
    "rcn_ref",
    "expected_rwn_route_relations",
    "expected_rcn_route_relations",
    "expected_rhn_route_relations",
    "expected_rmn_route_relations",
    "expected_rpn_route_relations",
    "expected_rin_route_relations",
    "network:type"
  )

  def apply(tagable: Tagable): TagFilter = {
    apply(tagable.tags)
  }

  def apply(tags: Tags): TagFilter = {
    new TagFilter(standardTagKeys, tags)
  }
}

object RouteTagFilter {

  val standardTagKeys = Seq(
    "note",
    "network",
    "type",
    "route",
    "network:type"
  )

  def apply(tagable: Tagable): TagFilter = {
    apply(tagable.tags)
  }

  def apply(tags: Tags): TagFilter = {
    new TagFilter(standardTagKeys, tags)
  }
}

object NetworkTagFilter {

  val standardTagKeys = Seq(
    "network",
    "type",
    "name",
    "network:type"
  )

  def apply(tagable: Tagable): TagFilter = {
    apply(tagable.tags)
  }

  def apply(tags: Tags): TagFilter = {
    new TagFilter(standardTagKeys, tags)
  }
}

object WayTagFilter {

  def apply(tagable: Tagable): TagFilter = {
    apply(tagable.tags)
  }

  def apply(tags: Tags): TagFilter = {
    new TagFilter(Seq(), tags)
  }
}

class TagFilter(standardTagKeys: Seq[String], tags: Tags) {

  def isEmpty: Boolean = tags.isEmpty

  def standardTags: Seq[Tag] = standardTagKeys.flatMap { tagKey =>
    tags(tagKey) match {
      case Some(value) => Some(Tag(tagKey, value))
      case _ => None
    }
  }

  def extraTags: Seq[Tag] = tags.tags.flatMap { case (Tag(key, value)) =>
    if (standardTagKeys.contains(key)) {
      None
    }
    else {
      Some(Tag(key, value))
    }
  }.sortWith(_.key < _.key)

  def hasExtraTags: Boolean = tags.tags.exists { case (Tag(key, value)) => !standardTagKeys.contains(key)}
}
