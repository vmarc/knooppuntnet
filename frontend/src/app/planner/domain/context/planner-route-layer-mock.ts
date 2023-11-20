import { Map } from 'immutable';
import { PlanLeg } from '../plan/plan-leg';
import { PlannerRouteLayer } from './planner-route-layer';

export class PlannerRouteLayerMock extends PlannerRouteLayer {
  private legs: Map<string, PlanLeg> = Map<string, PlanLeg>();

  addPlanLeg(leg: PlanLeg): void {
    this.legs = this.legs.set(leg.featureId, leg);
  }

  removePlanLeg(legId: string): void {
    this.legs = this.legs.remove(legId);
  }

  expectRouteLegCount(count: number): void {
    if (this.legs.size !== count) {
      let message = `the expected number of route legs (${count}) does not match the actual (${this.legs.size}) number of legs`;
      if (!this.legs.isEmpty()) {
        message += `, the route-layer contains following leg(s): `;
        message += this.legs
          .map((leg) => `${leg.featureId}(${leg.key})`)
          .join(', ');
      }
      throw new Error(message);
    }
  }

  expectRouteLegExists(legId: string, leg: PlanLeg): void {
    const routeLeg = this.legs.get(legId);
    expect(routeLeg).toBeDefined();
    expect(routeLeg.featureId).toEqual(leg.featureId);
  }
}
