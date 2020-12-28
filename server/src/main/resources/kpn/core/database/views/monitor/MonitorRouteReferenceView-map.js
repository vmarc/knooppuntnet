if (doc.monitorRouteState) {
  var routeId = doc.monitorRouteState.routeId;
  var referenceKey = doc.monitorRouteState.referenceKey;
  if (referenceKey) {
    emit(routeId, referenceKey);
  }
}
