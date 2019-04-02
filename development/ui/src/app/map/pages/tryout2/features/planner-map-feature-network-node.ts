import {PlannerMapFeature} from "./planner-map-feature";
import Coordinate from 'ol/View';

export class PlannerMapFeatureNetworkNode extends PlannerMapFeature {

  constructor(public readonly nodeId: string,
              public readonly nodeName: string,
              public readonly coordinate: Coordinate) {
    super();
  }

  isNetworkNode() {
    return true;
  }

}
