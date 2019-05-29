import TileLayer from "ol/layer/Tile";
import TileDebug from "ol/source/TileDebug";
import {createXYZ} from "ol/tilegrid";

export class DebugLayer {

  public static build(): TileLayer {

    const tileGrid = createXYZ({
      tileSize: 512,
      maxZoom: 20
    });

    return new TileLayer({
      source: new TileDebug({
        projection: "EPSG:3857",
        tileGrid: tileGrid
      })
    });
  }

}
