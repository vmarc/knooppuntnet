import Coordinate from "ol/coordinate";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanNode} from "../plan/plan-node";

export class PlannerMapFeature {

  constructor(readonly featureId: string,
              readonly legId: string,
              readonly flagType: PlanFlagType,
              readonly node: PlanNode) {
  }

  isLeg() {
    return this.featureId != null && this.legId != null && this.flagType == null && this.node == null;
  }

  isFlag() {
    return this.featureId != null && this.flagType != null;
  }

  isNetworkNode() {
    return this.featureId != null && this.node != null && this.legId == null && this.flagType == null;
  }

  static networkNode(nodeId: string, nodeName: string, coordinate: Coordinate): PlannerMapFeature {
    return new PlannerMapFeature(nodeId, null, null, PlanNode.create(nodeId, nodeName, coordinate));
  }

  static leg(featureId: string): PlannerMapFeature {
    return new PlannerMapFeature(featureId, featureId, null, null);
  }

  static flag(flagType: PlanFlagType, featureId: string): PlannerMapFeature {
    return new PlannerMapFeature(featureId, null, flagType, null);
  }

  static startFlag(featureId: string): PlannerMapFeature {
    return new PlannerMapFeature(featureId, null, PlanFlagType.Start, null);
  }

  static viaFlag(featureId: string): PlannerMapFeature {
    return new PlannerMapFeature(featureId, null, PlanFlagType.Via, null);
  }
}
