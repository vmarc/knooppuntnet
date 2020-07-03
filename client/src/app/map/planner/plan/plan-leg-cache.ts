import {Map} from "immutable";
import {PlanLeg} from "./plan-leg";

export class PlanLegCache {

  private legs: Map<string, PlanLeg> = Map();

  add(leg: PlanLeg) {
    this.legs = this.legs.set(leg.featureId, leg);
  }

  get(legKey: string): PlanLeg {
    return this.legs.find(leg => leg.key === legKey);
  }

  getById(legId: string): PlanLeg {
    return this.legs.get(legId);
  }

}
