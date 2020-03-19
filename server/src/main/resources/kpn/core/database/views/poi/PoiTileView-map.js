if (doc && doc.poi && doc.poi.tiles) {
  var poi = doc.poi;
  if (poi.layers && poi.layers.length > 0) {
    for (var i = 0; i < poi.tiles.length; i++) {
      var tile = poi.tiles[i];
      var key = [
        tile,
        poi.elementType,
        poi.elementId
      ];
      var value = [
        poi.layers[0],
        poi.latitude,
        poi.longitude
      ];
      emit(key, value);
    }
  }
}
