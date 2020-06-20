import {Map} from "immutable";
import {PlanLeg} from "./plan-leg";
import {ViaRoute} from "./via-route";

export class PlanLegCache {

  private legs: Map<string, PlanLeg> = Map();

  add(leg: PlanLeg) {
    this.legs = this.legs.set(leg.featureId, leg);
  }

  get(sourceNodeId: string, sinkNodeId: string, viaRoute: ViaRoute): PlanLeg {
    return this.legs.find(leg => {
      return leg.source.nodeId === sourceNodeId && leg.sink.nodeId === sinkNodeId
        && (
          viaRoute === null
          ||
          viaRoute !== null && leg.viaRoute.routeId === viaRoute.routeId && leg.viaRoute.pathId === viaRoute.pathId
        );
    });
  }

  getById(legId: string): PlanLeg {
    return this.legs.get(legId);
  }

}
