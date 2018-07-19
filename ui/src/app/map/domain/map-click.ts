import {MapState} from "./map-state";
import {SelectedFeatureHolder} from "./selected-feature-holder";

import Select from 'ol/interaction/Select';
import {click} from 'ol/events/condition';
import Style from 'ol/style/Style';
import {SelectedFeature} from "./selected-feature";

export class MapClick {

  readonly interaction = new Select({
    condition: click,
    multi: false,
    style: new Style() // this overrides the normal openlayers default edit style
  });

  constructor(
    state: MapState,
    selectionHolder: SelectedFeatureHolder,
    refreshCallback: () => void) {

    this.interaction.on("select", (e) => this.selectListener(state, selectionHolder, refreshCallback, e));
  }

  private selectListener(
    mapState: MapState,
    selectionHolder: SelectedFeatureHolder,
    refreshCallback: () => void,
    e /*: ol.interaction.select.Event*/): boolean {
    for (let feature of e.deselected) {
      const layer = feature.get("layer");
      if (layer.endsWith("route")) {
        mapState.selectedRouteId = null;
      }
      else if (layer.endsWith("node")) {
        mapState.selectedNodeId = null;
      }
    }

    if (e.selected.isEmpty) {
      selectionHolder.select(null)
    }
    else {
      for (let feature of e.selected) {
        const layer = feature.get("layer");
        if (layer.endsWith("route")) {
          const id = feature.get("id");
          const id2 = id.substring(0, id.indexOf('-'));
          const name = feature.get("name");
          mapState.selectedRouteId = id2 + "-";
          const selection = new SelectedFeature(+id2, name, "route");
          selectionHolder.select(selection)
        }
        else if (layer.endsWith("node")) {
          mapState.selectedRouteId = null;
          const id = feature.get("id");
          mapState.selectedNodeId = id;
          const name = feature.get("name");
          const selection = new SelectedFeature(+id, name, "node");
          selectionHolder.select(selection);
        }
      }
    }

    refreshCallback();

    return true;
  }

}
