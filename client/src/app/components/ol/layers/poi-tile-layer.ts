import MVT from "ol/format/MVT";
import VectorTileLayer from "ol/layer/VectorTile";
import VectorTile from "ol/source/VectorTile";
import {ZoomLevel} from "../domain/zoom-level";

export class PoiTileLayer {

  build(): VectorTileLayer {

    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.poiTileMinZoom,
      maxZoom: ZoomLevel.poiTileMaxZoom,
      format: new MVT(),
      url: "/tiles/poi/{z}/{x}/{y}.mvt"
    });

    return new VectorTileLayer({
      source: source,
      renderMode: "image"
    });
  }

}
