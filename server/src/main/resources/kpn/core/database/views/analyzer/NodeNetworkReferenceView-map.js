if (doc && doc.network && doc.network.active === true) {

  var attributes = doc.network.attributes;
  var detail = doc.network.detail;

  if (detail) {
    for (var i = 0; i < detail.nodes.length; i++) {
      var node = detail.nodes[i];
      var key = [
        node.id,
        attributes.id
      ];

      var value = {
        networkType: attributes.networkType,
        networkScope: attributes.networkScope,
        id: attributes.id,
        name: attributes.name
      };

      emit(key, value);
    }
  }
}
