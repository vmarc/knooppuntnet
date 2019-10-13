package kpn.server.analyzer.engine.changes.changes

import kpn.core.common.TimestampUtil
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.data.Tag
import kpn.shared.data.Tags

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
      createdAt,
      closedAt,
      open,
      commentsCount.toInt,
      tags
    )
  }
}
