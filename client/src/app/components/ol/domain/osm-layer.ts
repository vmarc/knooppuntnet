import {applyStyle} from "ol-mapbox-style";
import MVT from "ol/format/MVT";
import TileLayer from "ol/layer/Tile";
import VectorTileLayer from "ol/layer/VectorTile";
import VectorTile from "ol/source/VectorTile";
import {createXYZ} from "ol/tilegrid";

export class OsmLayer {

  public static build(): TileLayer {

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

    fetch("assets/style.json").then((response) => {
      response.json().then((glStyle) => {
        applyStyle(layer, glStyle, "openmaptiles");
      });
    });

    return layer;
  }

}
