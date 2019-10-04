if (doc && doc.node && doc.node.active === true) {
  var n = doc.node;
  if (n.active === true && n.orphan === true) {
    if (n.rwnName.length > 0) {
      var key = [
        n.orphan,
        n.display,
        n.country,
        "rwn",
        n.id
      ];
      emit(key, n);
    }
    if (n.rcnName.length > 0) {
      var key = [
        n.orphan,
        n.display,
        n.country,
        "rcn",
        n.id
      ];
      emit(key, n);
    }
  }
}
