package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait FeatureLayer extends EnumEntry with Hyphencase

object FeatureLayer extends Enum[FeatureLayer] {

  val values: IndexedSeq[FeatureLayer] = findValues

  // route tiles
  case object route extends FeatureLayer

  case object node extends FeatureLayer

  case object nodeRoute extends FeatureLayer

  case object errorNode extends FeatureLayer

  // other
  case object opendataNode extends FeatureLayer

  case object opendataRoute extends FeatureLayer

  case object relation extends FeatureLayer

  case object leg extends FeatureLayer

  case object flag extends FeatureLayer

  case object nodeMarker extends FeatureLayer
}
