import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";

export abstract class PlannerRouteLayer {

  abstract addPlanLeg(leg: PlanLeg): void;

  abstract removePlanLeg(legId: string): void;

  removePlan(plan: Plan): void {
    plan.legs.forEach(leg => {
      this.removePlanLeg(leg.featureId);
    });
  }

  addPlan(plan: Plan): void {
    for (let i = 0; i < plan.legs.size; i++) {
      const leg = plan.legs.get(i);
      this.addPlanLeg(leg);
    }
  }

}
