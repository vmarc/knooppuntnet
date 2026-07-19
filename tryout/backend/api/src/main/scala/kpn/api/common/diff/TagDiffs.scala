package kpn.api.common.diff

case class TagDiffs(
  mainTags: Seq[TagDiff] = Seq.empty, // display above separator line
  extraTags: Seq[TagDiff] = Seq.empty // display below separator line
) {

  def hasBothTagTypes: Boolean = mainTags.nonEmpty && extraTags.nonEmpty // OR different name: #hasSeparator
}
