package kpn.api.common.diff

import kpn.api.common.common.ToStringBuilder

case class TagDiffs(
  mainTags: Seq[TagDetail] = Seq.empty, // display above separator line
  extraTags: Seq[TagDetail] = Seq.empty // display below separator line
) {

  def hasBothTagTypes: Boolean = mainTags.nonEmpty && extraTags.nonEmpty // OR different name: #hasSeparator

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("mainTags", mainTags).
    field("extraTags", extraTags).
    build
}
