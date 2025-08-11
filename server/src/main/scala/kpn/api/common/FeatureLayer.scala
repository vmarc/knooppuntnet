package kpn.api.common

enum FeatureLayer:
  case route,
   node,
   nodeRoute,
   errorNode,
   errorOrphanNode,
   orphanNode,
   orphanRoute,
   incompleteRoute,
   errorRoute,
   opendataNode,
   opendataRoute,
   relation,
   leg,
   flag,
   nodeMarker

