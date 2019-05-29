import Map from "ol/Map";
import {MainStyleColors} from "./main-style-colors";
import {NodeStyle} from "./node-style";
import {RouteStyle} from "./route-style";

export class NodeMapStyle {

  private readonly smallNodeStyle = NodeStyle.smallNodeStyle();
  private readonly largeNodeStyle = NodeStyle.largeNodeStyle();
  private readonly routeStyle = new RouteStyle();

  constructor(private map: Map) {
  }

  public styleFunction() {
    return (feature, resolution) => {
      if (feature) {
        const zoom = this.map.getView().getZoom();
        const layer = feature.get("layer");
        if (layer.includes("node")) {
          if (zoom >= 13) {
            this.largeNodeStyle.getText().setText(feature.get("name"));
            return this.largeNodeStyle;
          }
          return this.smallNodeStyle;
        }
        return this.routeStyle.style(MainStyleColors.green, zoom, false);
      }
    };
  }
}
