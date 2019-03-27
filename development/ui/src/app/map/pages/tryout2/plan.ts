import {PlanMember} from "./plan-member";
import {List} from "immutable";
import Coordinate from 'ol/View';

export class Plan {

  constructor(public members: List<PlanMember>) {
  }

  /*
    Determines the user selected nodes that can be used to
    draw the elastic bands that can be shown when dragging
    a leg to another node.
  */
  findUserNodesForLegWithId(legId: string): [Coordinate, Coordinate] {
    // look through members to find first UserSelectedNode member before and after the leg with given id
    return null;
  }


  isStartNode(nodeId: string): boolean {
    // is first member in the plan
    return false;
  }

  isEndNode(nodeId: string): boolean {
    // is last member in the plan
    return false;
  }

  isUserSelectedNode(nodeId: string): boolean {
    // return true if can find member type  UserSelectedNode with given nodeId
    return false;
  }

  isServerPlannedNode(nodeId: string): boolean {
    return false;
  }

}
