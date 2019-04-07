import Map from 'ol/Map';
import {MapService} from "../map.service";
import {MainMapNodeStyle} from "./main-map-node-style";
import {MainMapRouteStyle} from "./main-map-route-style";

export class MainMapStyle {

  private readonly mainMapNodeStyle;
  private readonly mainMapRouteStyle;

  constructor(private map: Map, private mapService: MapService) {
    this.mainMapNodeStyle = new MainMapNodeStyle(mapService);
    this.mainMapRouteStyle = new MainMapRouteStyle(mapService);
  }

  public styleFunction() {
    return (feature, resolution) => {
      const properties = feature.getProperties();
      const zoom = this.map.getView().getZoom();
      const layer = feature.get("layer");
      if (layer.includes("node")) {
        return this.mainMapNodeStyle.nodeStyle(zoom, feature, true);
      }
      return this.mainMapRouteStyle.routeStyle(zoom, feature, layer, true);
    };
  }
}
