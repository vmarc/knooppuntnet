import Coordinate from "ol/coordinate";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import {Crosshair} from "./crosshair";
import {PlannerCrosshair} from "./planner-crosshair";

/*
  - displays crosshair to be used for selection of nodes
  - this is layer different from PlannerLayer because z-index on top
    of node/route vector layer, while PlannerLayer needs z-index under node/route layer
 */
export class PlannerCrosshairImpl implements PlannerCrosshair {

  private crosshair = new Crosshair();

  private source = new VectorSource({
    features: this.crosshair.getFeatures()
  });

  private layer = new VectorLayer({
    source: this.source
  });

  constructor() {
    // TODO set layer zoom level min and max, so that crosshair not visible at zoom levels where vector tiles are not available anymore
    // TODO set crosshair initially hidden
  }

  addToMap(map: Map) {
    map.addLayer(this.layer);
    // TODO listen to zoom changes to change to dimensions of the crosshair depending on the zoomlevel
    // ==> no need for this if we use Feature style to set circle radius?? --> stil need to make it smaller when nodes are drawn smaller also!!
    // See: https://gis.stackexchange.com/questions/58570/openlayers-make-polygons-not-zoom
  }

  setVisible(visible: boolean) {
    this.layer.setVisible(visible);
  }

  updatePosition(coordinate: Coordinate) {
    this.crosshair.updatePosition(coordinate);
  }
}
