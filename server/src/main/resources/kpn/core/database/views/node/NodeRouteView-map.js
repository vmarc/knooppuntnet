if (doc && doc.nodeRoute) {
  emit([doc.nodeRoute.networkType, doc.nodeRoute.networkScope, doc.nodeRoute.id], 1);
}
