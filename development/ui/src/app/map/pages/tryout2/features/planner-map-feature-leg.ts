import {PlannerMapFeature} from "./planner-map-feature";

export class PlannerMapFeatureLeg extends PlannerMapFeature {

  constructor(public readonly legId: string) {
    super();
  }

  isLeg() {
    return true;
  }

}
