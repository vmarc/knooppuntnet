import Feature from "ol/Feature";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import {MapService} from "../map.service";
import {MainStyleColors} from "./main-style-colors";
import {RouteStyle} from "./route-style";

export class MainMapRouteStyle {

  private readonly routeStyleBuilder = new RouteStyle();
  private readonly defaultRouteSelectedStyle = this.initRouteSelectedStyle();

  constructor(private mapService: MapService) {
  }

  public routeStyle(zoom: number, feature: Feature, layer: string, enabled: boolean): Array<Style> {
    const selectedStyle = this.determineRouteSelectedStyle(feature);
    const style = this.determineRouteStyle(feature, layer, enabled, zoom);
    return selectedStyle ? [selectedStyle, style] : [style];
  }

  private determineRouteSelectedStyle(feature: Feature): Style {
    const featureId = feature.get("id");
    let style = null;
    if (this.mapService.selectedRouteId && featureId && featureId.startsWith(this.mapService.selectedRouteId)) {
      style = this.defaultRouteSelectedStyle;
    }
    return style;
  }

  private determineRouteStyle(feature: Feature, layer: string, enabled: boolean, zoom: number): Style {

    const color = this.routeColor(layer, enabled);
    const highligthed = this.mapService.highlightedRouteId && feature.get("id").startsWith(this.mapService.highlightedRouteId);

    return this.routeStyleBuilder.style(color, zoom, highligthed);
  }

  private initRouteStyle() {
    return new Style({
      stroke: new Stroke({
        color: MainStyleColors.green,
        width: 1
      })
    });
  }

  private initRouteSelectedStyle(): Style {
    return new Style({
      stroke: new Stroke({
        color: MainStyleColors.yellow,
        width: 14
      })
    });
  }

  private routeColor(layer: string, enabled: boolean) /*ol.Color*/ {
    let color = MainStyleColors.gray;
    if (enabled) {
      if ("route" === layer) {
        color = MainStyleColors.green;
      } else if ("orphan-route" === layer) {
        color = MainStyleColors.darkGreen;
      } else if ("incomplete-route" === layer) {
        color = MainStyleColors.red;
      } else if ("error-route" === layer) {
        color = [255, 165, 0]; // ol.Color
      }
    }
    return color;
  }

}
