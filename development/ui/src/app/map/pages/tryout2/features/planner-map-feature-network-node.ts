import Coordinate from 'ol/coordinate';
import {PlannerMapFeature} from "./planner-map-feature";

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
