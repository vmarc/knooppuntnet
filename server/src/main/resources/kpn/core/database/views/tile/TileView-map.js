if (doc) {
  if (doc.route && doc.route.active === true) {
    for (i = 0; i < doc.route.tiles.length; i++) {
      emit([doc.route.tiles[i], "route", doc.route.summary.id], 1);
    }
  }
  else if (doc.node && doc.node.active === true) {
    for (i = 0; i < doc.node.tiles.length; i++) {
      emit([doc.node.tiles[i], "node", doc.node.id], 1);
    }
  }
}
