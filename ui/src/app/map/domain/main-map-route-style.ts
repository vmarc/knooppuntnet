import Style from 'ol/style/Style';
import Stroke from 'ol/style/Stroke';
import {MainStyleColors} from "./main-style-colors";
import {MapState} from "./map-state";

export class MainMapRouteStyle {

  private readonly routeSelectedStyle = new Style({
    stroke: new Stroke({
      color: [255, 255, 0],  // ol.Color
      width: 14
    })
  });

  private readonly routeStyle = new Style({
    stroke: new Stroke({
      color: MainStyleColors.green,
      width: 1
    })
  });

  public createRouteStyle(state: MapState, zoom: number, feature /*: ol.render.Feature*/, layer: string, enabled: boolean): Style[] {

    let routeColor = MainStyleColors.gray;
    if (enabled) {
      if ("route" == layer) {
        routeColor = MainStyleColors.green;
      }
      else if ("orphan-route" == layer) {
        routeColor = MainStyleColors.darkGreen;
      }
      else if ("incomplete-route" == layer) {
        routeColor = MainStyleColors.red;
      }
      else if ("error-route" == layer) {
        routeColor = [255, 165, 0]; // ol.Color
      }
    }

    const featureId = feature.get("id");

    let selectedStyle = null;
    if (state.selectedRouteId && featureId && featureId.startsWith(state.selectedRouteId)) {
      selectedStyle = this.routeSelectedStyle;
    }

    this.routeStyle.getStroke().setColor(routeColor);

    if (zoom < 9) {
      this.routeStyle.getStroke().setWidth(1);
    }
    else if (zoom < 12) {
      this.routeStyle.getStroke().setWidth(2);
    }
    else {
      if (state.highlightedRouteId && feature.get("id").startsWith(state.highlightedRouteId)) {
        this.routeStyle.getStroke().setWidth(8)
      }
      else {
      this.routeStyle.getStroke().setWidth(4)
      }
    }

    return selectedStyle ? [selectedStyle, this.routeStyle] : [this.routeStyle];
  }
}
