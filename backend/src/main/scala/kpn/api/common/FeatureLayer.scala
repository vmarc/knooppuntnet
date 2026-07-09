package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait FeatureLayer extends EnumEntry with Hyphencase

object FeatureLayer extends Enum[FeatureLayer] {

  val values: IndexedSeq[FeatureLayer] = findValues

  // route tiles
  final case object route extends FeatureLayer

  final case object node extends FeatureLayer

  final case object nodeRoute extends FeatureLayer

  final case object errorNode extends FeatureLayer

  // other
  final case object opendataNode extends FeatureLayer

  final case object opendataRoute extends FeatureLayer

  final case object relation extends FeatureLayer

  final case object leg extends FeatureLayer

  final case object flag extends FeatureLayer

  final case object nodeMarker extends FeatureLayer
}
