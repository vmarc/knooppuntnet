import Map from "ol/Map";
import Feature from "ol/Feature";
import {MapState} from "../map-state";

export class MapMoveHandler {

  constructor(private map: Map,
              private mapState: MapState) {
  }

  public handle(e /*: ol.interaction.select.Event*/) {
    this.updateCursor(e);
  }

  private updateCursor(e) {
    if (e.selected.length > 0) {
      this.map.getTargetElement().setAttribute("style", "cursor: pointer");
    } else {
      this.map.getTargetElement().setAttribute("style", "cursor: default");
    }
  }

  private handleDeselectedFeatures(features: Feature[]) {
    for (const feature of features) {
      const layer = feature.values_.layer;

      if (layer !== undefined) {
        if (layer.endsWith("route")) {
          this.mapState.highlightedRouteId = null;
        } else if (layer.endsWith("node")) {
          this.mapState.highlightedNodeId = null;
        }
      }
    }
  }

  private handleSelectedFeatures(features: Feature[]) {
    for (const feature of features) {
      const layer = feature.values_.layer;
      const id = feature.values_.id;

      if (layer !== undefined) {
        if (layer.endsWith("route")) {
          this.mapState.highlightedRouteId = id.substring(0, id.indexOf("-"));
        } else if (layer.endsWith("node")) {
          this.mapState.highlightedRouteId = null;
          this.mapState.highlightedNodeId = id;
        }
      }
    }
  }
}
