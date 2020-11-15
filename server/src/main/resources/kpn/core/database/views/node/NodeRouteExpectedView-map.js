if (doc && doc.node && doc.node.tags && doc.node.active === true) {
  var node = doc.node;
  for (var i = 0; i < node.tags.length; i++) {
    var tag = node.tags[i];
    var key = tag[0];
    var value = tag[1];
    if (key === "expected_rwn_route_relations") {
      emit(["hiking", node.id], value);
    } else if (key === "expected_rcn_route_relations") {
      emit(["cycling", node.id], value);
    } else if (key === "expected_rhn_route_relations") {
      emit(["horse-riding", node.id], value);
    } else if (key === "expected_rmn_route_relations") {
      emit(["motorboat", node.id], value);
    } else if (key === "expected_rpn_route_relations") {
      emit(["canoe", node.id], value);
    } else if (key === "expected_rin_route_relations") {
      emit(["inline-skating", node.id], value);
    }
  }
}
