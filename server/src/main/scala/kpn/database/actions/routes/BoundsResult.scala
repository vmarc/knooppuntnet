package kpn.database.actions.routes

import kpn.api.common.Bounds
import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class BoundsResult(bounds: Bounds) extends Storable
