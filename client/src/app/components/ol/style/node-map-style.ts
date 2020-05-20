import Map from "ol/Map";
import {StyleFunction} from "ol/style/Style";
import {MainStyleColors} from "./main-style-colors";
import {NodeStyle} from "./node-style";
import {RouteStyle} from "./route-style";

export class NodeMapStyle {

  private readonly smallNodeStyle = NodeStyle.smallGreen;
  private readonly largeNodeStyle = NodeStyle.largeGreen;
  private readonly routeStyle = new RouteStyle();

  constructor(private map: Map) {
  }

  public styleFunction(): StyleFunction {
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
