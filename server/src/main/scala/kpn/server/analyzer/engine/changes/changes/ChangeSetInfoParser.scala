package kpn.server.analyzer.engine.changes.changes

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.common.TimestampUtil

class ChangeSetInfoParser {

  def parse(xml: scala.xml.Node): ChangeSetInfo = {
    val changeSet = xml.child \\ "changeset"
    val id = (changeSet \ "@id").text
    val createdAt = TimestampUtil.parseIso((changeSet \ "@created_at").text)
    val closedAtAttribute = changeSet \ "@closed_at"
    val closedAt = if (closedAtAttribute.isEmpty) None else Some(TimestampUtil.parseIso(closedAtAttribute.text))
    val open = (changeSet \ "@open").text == "true"
    val commentsCount = (changeSet \ "@comments_count").text
    val tags = Tags(
      (changeSet \ "tag").map { tag =>
        val key = (tag \ "@k").text
        val value = (tag \ "@v").text
        Tag(key, value)
      }
    )
    ChangeSetInfo(
      id.toLong,
      id.toLong,
      createdAt,
      closedAt,
      open,
      commentsCount.toInt,
      tags
    )
  }
}
