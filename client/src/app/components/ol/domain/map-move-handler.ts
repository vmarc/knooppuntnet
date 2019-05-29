import Feature from "ol/Feature";
import SelectEvent from "ol/interaction/Select";
import Map from "ol/Map";
import {MapService} from "../map.service";

export class MapMoveHandler {

  constructor(private map: Map, private mapService: MapService) {
  }

  public handle(e: SelectEvent) {
    this.updateCursor(e);
    this.handleDeselectedFeatures(e.deselected);
    this.handleSelectedFeatures(e.selected);
  }

  private updateCursor(e: SelectEvent) {
    if (e.selected.length > 0) {
      this.map.getTargetElement().setAttribute("style", "cursor: pointer")
    } else {
      this.map.getTargetElement().setAttribute("style", "cursor: default")
    }
  }

  private handleDeselectedFeatures(features: Array<Feature>) {
    for (let feature of features) {
      const layer = feature.get("layer");
      if (layer.endsWith("route")) {
        this.mapService.highlightedRouteId = null
      } else if (layer.endsWith("node")) {
        this.mapService.highlightedNodeId = null
      }
    }
  }

  private handleSelectedFeatures(features: Array<Feature>) {
    for (let feature of features) {
      const layer = feature.get("layer");
      const id = feature.get("id");
      if (layer.endsWith("route")) {
        const routeId = id.substring(0, id.indexOf("-"));
        this.mapService.highlightedRouteId = routeId;
      } else if (layer.endsWith("node")) {
        this.mapService.highlightedRouteId = null;
        this.mapService.highlightedNodeId = id;
      }
    }
  }

}
