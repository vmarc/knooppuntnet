import {BehaviorSubject, Observable} from "rxjs";
import {NetworkType} from "../../../kpn/shared/network-type";
import {PlannerCommand} from "../commands/planner-command";
import {PlannerCommandStack} from "../commands/planner-command-stack";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlannerCrosshair} from "./planner-crosshair";
import {PlannerCursor} from "./planner-cursor";
import {PlannerElasticBand} from "./planner-elastic-band";
import {PlannerLegRepository} from "./planner-leg-repository";
import {PlannerMode} from "./planner-mode";
import {PlannerRouteLayer} from "./planner-route-layer";

export class PlannerContext {

  private _mode = new BehaviorSubject<PlannerMode>(PlannerMode.Idle);
  private _plan = new BehaviorSubject<Plan>(Plan.empty());
  private _networkType = new BehaviorSubject<NetworkType>(null);

  constructor(readonly commandStack: PlannerCommandStack,
              readonly routeLayer: PlannerRouteLayer,
              readonly crosshair: PlannerCrosshair,
              readonly cursor: PlannerCursor,
              readonly elasticBand: PlannerElasticBand,
              readonly legRepository: PlannerLegRepository,
              readonly legs: PlanLegCache) {
  }

  setNetworkType(networkType: NetworkType): void {
    this._networkType.next(networkType);
  }

  get networkType(): NetworkType {
    return this._networkType.value;
  }

  execute(command: PlannerCommand): void {
    this.commandStack.push(command);
    command.do(this);
  }

  undo(): void {
    const command = this.commandStack.undo();
    command.undo(this);
  }

  redo(): void {
    const command = this.commandStack.redo();
    command.do(this);
  }

  get mode(): Observable<PlannerMode> {
    return this._mode;
  }

  get planObserver(): Observable<Plan> {
    return this._plan;
  }

  get plan(): Plan {
    return this._plan.value;
  }

  updatePlan(plan: Plan) {
    this._plan.next(plan);
  }

  updatePlanLeg(newLeg: PlanLeg) {
    const newLegs = this.plan.legs.map(leg => {
      if (leg.featureId === newLeg.featureId) {
        return newLeg;
      }
      return leg;
    });
    const newPlan = Plan.create(this.plan.source, newLegs);
    this.updatePlan(newPlan);
    this.routeLayer.addRouteLeg(newLeg);
  }

}
