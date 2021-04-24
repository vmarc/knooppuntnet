if (doc && doc.nodeRoute) {
  emit([doc.nodeRoute.scopedNetworkType, doc.nodeRoute.id], 1);
}
