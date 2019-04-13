import TileLayer from "ol/layer/Tile";
import OSM from "ol/source/OSM";

export class OsmLayer {

  public static build(): TileLayer {
    const osmLayer = new TileLayer({
      source: new OSM()
    });
    osmLayer.set("name", "OpenStreetMap");
    return osmLayer;
  }

}
