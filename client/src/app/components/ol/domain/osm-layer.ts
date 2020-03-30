import stylefunction from "ol-mapbox-style/dist/stylefunction";
import MVT from "ol/format/MVT";
import BaseLayer from "ol/layer/Base";
import VectorTileLayer from "ol/layer/VectorTile";
import VectorTile from "ol/source/VectorTile";
import {createXYZ} from "ol/tilegrid";
import {osmStyle} from "./style";

export class OsmLayer {

  public static build(): BaseLayer {

    const tileGrid = createXYZ({
      tileSize: 512,
      maxZoom: 14
    });

    const source = new VectorTile({
      format: new MVT(),
      tileGrid: tileGrid,
      url: "/tiles/osm/{z}/{x}/{y}.pbf",
      attributions: [
        "<a href=\"https://www.openmaptiles.org/\" target=\"_blank\">&copy; OpenMapTiles</a>",
        "<a href=\"https://www.openstreetmap.org/copyright\" target=\"_blank\">&copy; OpenStreetMap contributors</a>"
      ]
    });

    const layer = new VectorTileLayer({
      source: source,
      renderMode: "image"
    });

    stylefunction(layer, osmStyle, "openmaptiles");

    return layer;
  }

}
