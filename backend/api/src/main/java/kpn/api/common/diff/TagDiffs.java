package kpn.api.common.diff;

import kpn.api.common.diff.TagDiff;

import com.google.common.collect.ImmutableList;

public record TagDiffs(
  ImmutableList<TagDiff> mainTags,
  ImmutableList<TagDiff> extraTags
) {
}

/* TODO migrate
package kpn.api.common.diff

case class TagDiffs(
  mainTags: Seq[TagDiff] = Seq.empty, // display above separator line
  extraTags: Seq[TagDiff] = Seq.empty // display below separator line
) {

  def hasBothTagTypes: Boolean = mainTags.nonEmpty && extraTags.nonEmpty // OR different name: #hasSeparator
}

*/
