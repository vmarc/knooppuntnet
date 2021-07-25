package kpn.api.common.diff

case class TagDiffs(
  mainTags: Seq[TagDetail] = Seq.empty, // display above separator line
  extraTags: Seq[TagDetail] = Seq.empty // display below separator line
) {

  def hasBothTagTypes: Boolean = mainTags.nonEmpty && extraTags.nonEmpty // OR different name: #hasSeparator

}
