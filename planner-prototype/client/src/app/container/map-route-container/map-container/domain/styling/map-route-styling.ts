import {Injectable} from "@angular/core";
import Style from "ol/style/Style";
import Stroke from "ol/style/Stroke";
import Feature from "ol/Feature";
import {MapState} from "../map-state";
import {MapStylingColors} from "./map-styling-colors";

@Injectable()
export class MapRouteStyling {

  private readonly defaultRouteStyle = this.initRouteStyle();
  private readonly defaultRouteSelectedStyle = this.initRouteSelectedStyle();

  constructor(private mapState: MapState) {
  }

  public routeStyle(zoom: number, feature: Feature, layer: string, enabled: boolean): Style[] {
    const selectedStyle = this.determineRouteSelectedStyle(feature);
    const style = this.determineRouteStyle(feature, layer, enabled, zoom);
    return selectedStyle ? [selectedStyle, style] : [style];
  }

  private determineRouteSelectedStyle(feature: Feature): Style {
    const featureId = feature.get("id");
    let style = null;
    if (this.mapState.selectedRouteId && featureId && featureId.startsWith(this.mapState.selectedRouteId)) {
      style = this.defaultRouteSelectedStyle;
    }
    return style;
  }

  private determineRouteStyle(feature: Feature, layer: string, enabled: boolean, zoom: number): Style {

    const color = this.routeColor(layer, feature.values_.surface, enabled);
    this.defaultRouteStyle.getStroke().setColor(color);

    if (zoom < 9) {
      this.defaultRouteStyle.getStroke().setWidth(1);
    } else if (zoom < 12) {
      this.defaultRouteStyle.getStroke().setWidth(2);
    } else {
      if (this.mapState.highlightedRouteId && feature.get("id").startsWith(this.mapState.highlightedRouteId)) {
        this.defaultRouteStyle.getStroke().setWidth(8)
      } else {
        this.defaultRouteStyle.getStroke().setWidth(4)
      }
    }
    return this.defaultRouteStyle;
  }

  private initRouteStyle() {
    return new Style({
      stroke: new Stroke({
        color: MapStylingColors.green,
        width: 1
      })
    });
  }

  private initRouteSelectedStyle(): Style {
    return new Style({
      stroke: new Stroke({
        color: MapStylingColors.yellow,
        width: 14
      })
    });
  }

  private routeColor(layer: string, surface: string, enabled: boolean) {
    let color = MapStylingColors.gray;
    if (enabled) {
      if ("route" == layer && "unpaved" === surface) {
        color = MapStylingColors.gray;
      } else if ("route" == layer && "paved" === surface) {
        color = MapStylingColors.green;
      } else if ("orphan-route" == layer) {
        color = MapStylingColors.darkGreen;
      } else if ("incomplete-route" == layer) {
        color = MapStylingColors.red;
      } else if ("error-route" == layer) {
        color = MapStylingColors.black;
      }
    }
    return color;
  }

}
