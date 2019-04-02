import {PlannerMapFeature} from "./planner-map-feature";

export class PlannerMapFeatureLegNode extends PlannerMapFeature {

  constructor(public readonly id: string,
              public readonly nodeId: string) {
    super();
  }

  isLegNode() {
    return true;
  }

}
