import Map from 'ol/Map';
import {MainMapNodeStyle} from "./main-map-node-style";
import {MainMapRouteStyle} from "./main-map-route-style";
import {MapState} from "./map-state";

export class MainMapStyle {

  private readonly mainMapNodeStyle;
  private readonly mainMapRouteStyle;

  constructor(private map: Map,
              private mapState: MapState) {
    this.mainMapNodeStyle = new MainMapNodeStyle(mapState);
    this.mainMapRouteStyle = new MainMapRouteStyle(mapState);
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
