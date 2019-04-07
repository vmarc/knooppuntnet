import Feature from 'ol/Feature';
import {SelectEvent} from 'ol/interaction/Select';
import {MapService, PoiId} from "../map.service";
import {SelectedFeature} from "./selected-feature";

export class MapClickHandler {

  constructor(private mapService: MapService) {
  }

  public handle(e: SelectEvent) {
    this.handleDeselectedFeatures(e.deselected);
    this.handleSelectedFeatures(e.selected);
  }

  private handleDeselectedFeatures(features: Array<Feature>) {
    for (let feature of features) {
      const layer = feature.get("layer");
      if (layer.endsWith("route")) {
        this.mapService.selectedRouteId = null;
      } else if (layer.endsWith("node")) {
        this.mapService.selectedNodeId = null;
      }
    }
  }

  private handleSelectedFeatures(features: Array<Feature>) {
    if (features.length == 0) {
      this.mapService.selectedFeature.next(null);
    } else {
      for (let feature of features) {
        const layer = feature.get("layer");
        if (layer.endsWith("route")) {
          this.handleRouteSelection(feature);
        } else if (layer.endsWith("node")) {
          this.handleNodeSelection(feature);
        } else {
          const id = feature.get("id");
          const type = feature.get("type");
          if (type === "way" || type === "node" || type === "relation") {
            this.mapService.poiClicked(new PoiId(type, id));
          }
        }
      }
    }
  }

  private handleRouteSelection(feature: Feature) {
    const id = feature.get("id");
    const id2 = id.substring(0, id.indexOf('-'));
    const name = feature.get("name");
    this.mapService.selectedRouteId = id2 + "-";
    const selection = new SelectedFeature(+id2, name, "route");
    this.mapService.selectedFeature.next(selection)
  }

  private handleNodeSelection(feature: Feature) {
    this.mapService.selectedRouteId = null;
    const id = feature.get("id");
    this.mapService.selectedNodeId = id;
    const name = feature.get("name");
    const selection = new SelectedFeature(+id, name, "node");
    this.mapService.selectedFeature.next(selection);
  }

}
