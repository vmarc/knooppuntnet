import MVT from "ol/format/MVT";
import XYZ from "ol/source/XYZ";
import VectorTileSource from "ol/source/VectorTile";
import TileLayer from "ol/layer/Tile";
import VectorTileLayer from "ol/layer/VectorTile";
import Feature from "ol/Feature";

export function createCyclingPngLayer() {
  return new TileLayer({
    source: new XYZ({url: "/tiles/cycling/{z}/{x}/{y}.png"}),
  });
}

export function createCyclingMvtLayer() {
  return new VectorTileLayer({
    source: new VectorTileSource({
      url: "/tiles/cycling/{z}/{x}/{y}.mvt",
      format: new MVT({
        featureClass: Feature
      })
    }),
    renderMode: "image",
    zIndex: 25
  });
}
