import {SelectedFeatureHolder} from "./selected-feature-holder";
import Map from 'ol/Map';
import Feature from 'ol/Feature';
import {pointerMove} from 'ol/events/condition';
import {MapState} from "./map-state";

export class MapMoveHandler {

  constructor(private map: Map,
              private mapState: MapState,
              private selectionHolder: SelectedFeatureHolder) {
  }

  public handle(e /*: ol.interaction.select.Event*/) {
    this.updateCursor(e);
    this.handleDeselectedFeatures(e.deselected);
    this.handleSelectedFeatures(e.selected);
  }

  private updateCursor(e) {
    if (e.selected.length > 0) {
      this.map.getTargetElement().setAttribute("style", "cursor: pointer")
    }
    else {
      this.map.getTargetElement().setAttribute("style", "cursor: default")
    }
  }

  private handleDeselectedFeatures(features: Feature[]) {
    for (let feature of features) {
      const layer = feature.get("layer");
      if (layer.endsWith("route")) {
        this.mapState.highlightedRouteId = null
      }
      else if (layer.endsWith("node")) {
        this.mapState.highlightedNodeId = null
      }
    }
  }

  private handleSelectedFeatures(features: Feature[]) {
    for (let feature of features) {
      const layer = feature.get("layer");
      const id = feature.get("id");
      if (layer.endsWith("route")) {
        const routeId = id.substring(0, id.indexOf('-'));
        this.mapState.highlightedRouteId = routeId;
      }
      else if (layer.endsWith("node")) {
        this.mapState.highlightedRouteId = null;
        this.mapState.highlightedNodeId = id;
      }
    }
  }

}
