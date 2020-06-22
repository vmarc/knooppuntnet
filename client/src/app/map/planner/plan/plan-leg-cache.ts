import {Map} from "immutable";
import {PlanLeg} from "./plan-leg";

export class PlanLegCache {

  private legs: Map<string, PlanLeg> = Map();

  add(leg: PlanLeg) {
    this.legs = this.legs.set(leg.featureId, leg);
  }

  get(sourceNodeId: string, sinkNodeId: string): PlanLeg {
    return this.legs.find(leg => {
      return leg.source.nodeId === sourceNodeId && leg.sink.nodeId === sinkNodeId; // && this.legViaRoute(leg, viaRoute);
    });
  }

  getById(legId: string): PlanLeg {
    return this.legs.get(legId);
  }

}
