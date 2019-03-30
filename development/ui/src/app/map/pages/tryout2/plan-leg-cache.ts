import {PlanLeg} from "./plan-leg";
import {List} from "immutable";

export class PlanLegCache {

  private legs: List<PlanLeg> = List();

  add(leg: PlanLeg) {
    this.legs = this.legs.push(leg);
  }

  get(sourceNodeId: string, sinkNodeId: string): PlanLeg {
    return this.legs.find(leg => leg.source.nodeId === sourceNodeId && leg.sink.nodeId === sinkNodeId);
  }

  getById(legId: string): PlanLeg {
    return this.legs.find(leg => leg.legId === legId);
  }

}
