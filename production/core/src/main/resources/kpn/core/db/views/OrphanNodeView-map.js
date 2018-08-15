if (doc && doc.node) {
  var n = doc.node;
  if (n.active === true && (n.orphan === true || n.ignored === true)) {
    if (n.rwnName.length > 0) {
      var key = [
        n.ignored,
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
        n.ignored,
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
