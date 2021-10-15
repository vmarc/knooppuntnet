package kpn.core.taginfo

import java.text.SimpleDateFormat
import java.util.Date

case class TagInfo(
  data_format: Long = 1,
  data_url: String = "https://raw.githubusercontent.com/vmarc/knooppuntnet/develop/docs/taginfo.json",
  data_updated: String = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'").format(new Date()),
  project: TagInfoProject = TagInfoProject(),
  tags: Seq[TagInfoTag]
)
