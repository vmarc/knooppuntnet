import MVT from "ol/format/MVT";
import VectorTileLayer from "ol/layer/VectorTile";
import VectorTile from "ol/source/VectorTile";
import {createXYZ} from "ol/tilegrid";
import {ZoomLevel} from "../domain/zoom-level";

export class PoiTileLayer {

  build(): VectorTileLayer {

    const tileGrid = createXYZ({
      tileSize: 512,
      minZoom: ZoomLevel.poiTileMinZoom,
      maxZoom: ZoomLevel.poiTileMaxZoom
    });

    const source = new VectorTile({
      format: new MVT(),
      tileGrid: tileGrid,
      url: "/tiles/poi/{z}/{x}/{y}.mvt"
    });

    const layer = new VectorTileLayer({
      source: source,
      renderMode: "image"
    });

    return layer;
  }

}
