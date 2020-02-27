if (doc && doc.route && doc.route.active === true) {
  var routeAnalysis = doc.route.analysis;
  emit(doc.route.summary.id, routeAnalysis.geometryDigest);
}
