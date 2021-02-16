if (doc && doc.node && doc.node.tags && doc.node.active === true) {
  var node = doc.node;
  for (var i = 0; i < node.tags.tags.length; i++) {
    var tag = node.tags.tags[i];
    var key = tag.key;
    var value = tag.value;
    if (key.startsWith("expected_")) {
      if (key.endsWith("wn_route_relations")) {
        emit(["hiking", node.id], value);
      } else if (key.endsWith("cn_route_relations")) {
        emit(["cycling", node.id], value);
      } else if (key.endsWith("hn_route_relations")) {
        emit(["horse-riding", node.id], value);
      } else if (key.endsWith("mn_route_relations")) {
        emit(["motorboat", node.id], value);
      } else if (key.endsWith("pn_route_relations")) {
        emit(["canoe", node.id], value);
      } else if (key.endsWith("in_route_relations")) {
        emit(["inline-skating", node.id], value);
      }
    }
  }
}
