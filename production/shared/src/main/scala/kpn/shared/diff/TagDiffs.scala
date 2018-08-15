package kpn.shared.diff

case class TagDiffs(
  mainTags: Seq[TagDetail] = Seq(), // display above separator line
  extraTags: Seq[TagDetail] = Seq() // display below separator line
) {
  def hasBothTagTypes: Boolean = mainTags.nonEmpty && extraTags.nonEmpty // OR different name: #hasSeparator
}
