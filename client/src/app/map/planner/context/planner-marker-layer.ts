import {Coordinate} from 'ol/coordinate';
import {Plan} from '../plan/plan';
import {PlanFlag} from '../plan/plan-flag';

export abstract class PlannerMarkerLayer {

  abstract addFlag(flag: PlanFlag): void;

  abstract removeFlag(flag: PlanFlag): void;

  abstract removeFlagWithFeatureId(featureId: string): void;

  abstract updateFlag(flag: PlanFlag): void;

  abstract updateFlagCoordinate(featureId: string, coordinate: Coordinate): void;

  removePlan(plan: Plan): void {
    this.removeFlag(plan.sourceFlag);
    plan.legs.forEach(leg => {
      this.removeFlag(leg.viaFlag);
      this.removeFlag(leg.sinkFlag);
    });
  }

  addPlan(plan: Plan): void {
    this.addFlag(plan.sourceFlag);
    for (let i = 0; i < plan.legs.size; i++) {
      const leg = plan.legs.get(i);
      this.addFlag(leg.viaFlag);
      this.addFlag(leg.sinkFlag);
    }
  }

}
