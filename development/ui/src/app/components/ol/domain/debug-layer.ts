import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import TileDebug from 'ol/source/TileDebug';

export class DebugLayer {

  public static build(): TileLayer {
    return new TileLayer({
      source: new TileDebug({
        projection: "EPSG:3857",
        tileGrid: new OSM().getTileGrid()
      })
    });
  }

}
