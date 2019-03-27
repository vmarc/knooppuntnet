import {PlanMemberType} from "./plan-member-type";
import Coordinate from 'ol/View';
import {PlanAbstractMember} from "./plan-abstract-member";

export class PlanNode extends PlanAbstractMember {

  constructor(public type: PlanMemberType, // UserSelectedNode or ServerPlannedNode
              public coordinate: Coordinate) {
    super();
  }

  isUserSelectedNode(): boolean {
    return this.type === PlanMemberType.UserSelectedNode;
  }

  isServerPlannedNode(): boolean {
    return this.type === PlanMemberType.ServerPlannedNode;
  }

  toNode(): PlanNode {
    return this;
  }

}
