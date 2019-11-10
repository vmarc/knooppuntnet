package kpn.api.common.route

sealed trait WayDirection
// note: TypescriptTool does not handle this ok --> adjust TypescriptTool.ignoredClasses if this ever changes

case object Both extends WayDirection // the way can be travelled in both directions
case object Forward extends WayDirection // the way can only be travelled in the forward direction
case object Backward extends WayDirection // the way can only be travelled in the backward direction
