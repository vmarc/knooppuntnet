import OSM from "ol/source/OSM";
import TileLayer from "ol/layer/Tile.js";

export function createBaseLayer() {
  return new TileLayer({
    source: new OSM(),
    type: "base"
  });
}
