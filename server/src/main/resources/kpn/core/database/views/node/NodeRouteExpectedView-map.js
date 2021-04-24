if (doc && doc.node && doc.node.tags && doc.node.active === true) {
  var node = doc.node;
  for (var i = 0; i < node.tags.tags.length; i++) {
    var tag = node.tags.tags[i];
    var key = tag.key;
    var value = tag.value;
    if (key.startsWith("expected_") && key.endsWith("n_route_relations")) {

      var networkTypeLetter = key.substr(10, 1);
      var networkScopeLetter = key.substr(9, 1);

      var networkType = "";
      if (networkTypeLetter === "w") {
        networkType = "hiking";
      } else if (networkTypeLetter === "c") {
        networkType = "cycling";
      } else if (networkTypeLetter === "h") {
        networkType = "horse-riding";
      } else if (networkTypeLetter === "m") {
        networkType = "motorboat";
      } else if (networkTypeLetter === "p") {
        networkType = "canoe";
      } else if (networkTypeLetter === "i") {
        networkType = "inline-skating";
      }

      var networkScope = "";
      if (networkScopeLetter === "l") {
        networkScope = "local";
      } else if (networkScopeLetter === "r") {
        networkScope = "regional";
      } else if (networkScopeLetter === "n") {
        networkScope = "national";
      } else if (networkScopeLetter === "i") {
        networkScope = "international";
      }

      if (networkType.length > 0 && networkScope.length > 0) {
        emit([networkType, networkScope, node.id], value);
      }
    }
  }
}
