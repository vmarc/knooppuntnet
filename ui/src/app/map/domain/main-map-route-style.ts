import Style from 'ol/style/Style';
import Stroke from 'ol/style/Stroke';
import Feature from 'ol/Feature';
import {MapState} from "./map-state";
import {MainStyleColors} from "./main-style-colors";

export class MainMapRouteStyle {

  private readonly defaultRouteStyle = this.initRouteStyle();
  private readonly defaultRouteSelectedStyle = this.initRouteSelectedStyle();

  constructor(private mapState: MapState) {
  }

  public routeStyle(zoom: number, feature: Feature, layer: string, enabled: boolean): Array<Style> {
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

    const color = this.routeColor(layer, enabled);
    this.defaultRouteStyle.getStroke().setColor(color);

    if (zoom < 9) {
      this.defaultRouteStyle.getStroke().setWidth(1);
    }
    else if (zoom < 12) {
      this.defaultRouteStyle.getStroke().setWidth(2);
    }
    else {
      if (this.mapState.highlightedRouteId && feature.get("id").startsWith(this.mapState.highlightedRouteId)) {
        this.defaultRouteStyle.getStroke().setWidth(8)
      }
      else {
        this.defaultRouteStyle.getStroke().setWidth(4)
      }
    }
    return this.defaultRouteStyle;
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
      if ("route" == layer) {
        color = MainStyleColors.green;
      }
      else if ("orphan-route" == layer) {
        color = MainStyleColors.darkGreen;
      }
      else if ("incomplete-route" == layer) {
        color = MainStyleColors.red;
      }
      else if ("error-route" == layer) {
        color = [255, 165, 0]; // ol.Color
      }
    }
    return color;
  }

}
