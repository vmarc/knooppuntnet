if (doc && doc.nodeRoute) {
  emit([doc.nodeRoute.networkType, doc.nodeRoute.id], 1);
}
