import Map from 'ol/Map';
import {MainMapNodeStyle} from "./main-map-node-style";
import {MainMapRouteStyle} from "./main-map-route-style";
import {MapState} from "./map-state";

export class MainMapStyle {

  private readonly nodeStyleBuilder = new MainMapNodeStyle();
  private readonly routeStyleBuilder = new MainMapRouteStyle();

  constructor(private map: Map,
              private mapState: MapState) {
  }

  public styleFunction() {
    return (feature, resolution) => {
      const properties = feature.getProperties();
      const zoom = this.map.getView().getZoom();
      const layer = feature.get("layer");
      if (layer.includes("node")) {
        return this.nodeStyleBuilder.createNodeStyle(this.mapState, zoom, feature, true)
      }
      return this.routeStyleBuilder.createRouteStyle(this.mapState, zoom, feature, layer, true)
    };
  }
}
