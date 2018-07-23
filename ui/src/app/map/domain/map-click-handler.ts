import {MapState} from "./map-state";
import Feature from 'ol/Feature';
import {click} from 'ol/events/condition';
import {SelectedFeature} from "./selected-feature";
import {SelectedFeatureHolder} from "./selected-feature-holder";

export class MapClickHandler {

  constructor(private mapState: MapState,
              private selectionHolder: SelectedFeatureHolder) {
  }

  public handle(e /*: ol.interaction.select.Event*/) {
    this.handleDeselectedFeatures(e.deselected);
    this.handleSelectedFeatures(e.selected);
  }

  private handleDeselectedFeatures(features: Feature[]) {
    for (let feature of features) {
      const layer = feature.get("layer");
      if (layer.endsWith("route")) {
        this.mapState.selectedRouteId = null;
      }
      else if (layer.endsWith("node")) {
        this.mapState.selectedNodeId = null;
      }
    }
  }

  private handleSelectedFeatures(features: Feature[]) {
    if (features.length == 0) {
      this.selectionHolder.select(null);
    }
    else {
      for (let feature of features) {
        const layer = feature.get("layer");
        if (layer.endsWith("route")) {
          this.handleRouteSelection(feature);
        }
        else if (layer.endsWith("node")) {
          this.handleNodeSelection(feature);
        }
      }
    }
  }

  private handleRouteSelection(feature: Feature) {
    const id = feature.get("id");
    const id2 = id.substring(0, id.indexOf('-'));
    const name = feature.get("name");
    this.mapState.selectedRouteId = id2 + "-";
    const selection = new SelectedFeature(+id2, name, "route");
    this.selectionHolder.select(selection)
  }

  private handleNodeSelection(feature: Feature) {
    this.mapState.selectedRouteId = null;
    const id = feature.get("id");
    this.mapState.selectedNodeId = id;
    const name = feature.get("name");
    const selection = new SelectedFeature(+id, name, "node");
    this.selectionHolder.select(selection);
  }

}
