import {SelectedFeatureHolder} from "./selected-feature-holder";
import Map from 'ol/Map';
import {MapState} from "./map-state";
import Select from 'ol/interaction/Select';
import {pointerMove} from 'ol/events/condition';
import Style from 'ol/style/Style';

export class MapMove {

  readonly interaction = new Select({
    condition: pointerMove,
    multi: false,
    style: new Style() // this overrides the normal openlayers default edit style
  });

  constructor(
    map: Map,
    state: MapState,
    selectionHolder: SelectedFeatureHolder,
    refreshCallback: () => void) {

    this.interaction.on("select", (e) => this.selectListener(map, state, selectionHolder, refreshCallback, e));
  }

  private selectListener(
    map: Map,
    mapState: MapState,
    selectionHolder: SelectedFeatureHolder,
    refreshCallback: () => void,
    e /*: ol.interaction.select.Event*/): boolean {

    if (e.selected.length > 0) {
      map.getTargetElement().setAttribute("style", "cursor: pointer")
    }
    else {
      map.getTargetElement().setAttribute("style", "cursor: default")
    }

    for (let feature of e.deselected) {
      const layer = feature.get("layer");
      if (layer.endsWith("route")) {
        mapState.highlightedRouteId = null
      }
      else if (layer.endsWith("node")) {
        mapState.highlightedNodeId = null
      }
    }

    for(let feature of e.selected) {
      const layer = feature.get("layer");
      const id = feature.get("id");
      if (layer.endsWith("route")) {
        const id2 = id.substring(0, id.indexOf('-'));
        mapState.highlightedRouteId = id2;
      }
      else if (layer.endsWith("node")) {
        mapState.highlightedRouteId = null;
        mapState.highlightedNodeId = id;
      }
    }

    refreshCallback();
    return true;
  }

}
