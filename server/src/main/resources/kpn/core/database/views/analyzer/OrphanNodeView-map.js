if (doc && doc.node) {
  var n = doc.node;
  if (n.active === true && n.orphan === true) {
    for (i = 0; i < n.names.length; i++) {
      var nodeName = n.names[i];
      var key = [
        n.country,
        nodeName.scopedNetworkType.networkType,
        n.id
      ];
      emit(key, n);
    }
  }
}
