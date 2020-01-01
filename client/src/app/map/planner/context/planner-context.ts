import {BehaviorSubject, Observable} from "rxjs";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerCommand} from "../commands/planner-command";
import {PlannerCommandStack} from "../commands/planner-command-stack";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlannerCursor} from "./planner-cursor";
import {PlannerElasticBand} from "./planner-elastic-band";
import {PlannerLegRepository} from "./planner-leg-repository";
import {PlannerMode} from "./planner-mode";
import {PlannerRouteLayer} from "./planner-route-layer";
import {PlannerOverlay} from "./planner-overlay";

export class PlannerContext {

  constructor(readonly commandStack: PlannerCommandStack,
              readonly routeLayer: PlannerRouteLayer,
              readonly cursor: PlannerCursor,
              readonly elasticBand: PlannerElasticBand,
              readonly legRepository: PlannerLegRepository,
              readonly legs: PlanLegCache,
              readonly overlay: PlannerOverlay) {
  }

  private _mode = new BehaviorSubject<PlannerMode>(PlannerMode.Idle);

  get mode(): Observable<PlannerMode> {
    return this._mode;
  }

  private _plan = new BehaviorSubject<Plan>(Plan.empty());

  get plan(): Plan {
    return this._plan.value;
  }

  private _networkType = new BehaviorSubject<NetworkType>(null);

  get networkType(): NetworkType {
    return this._networkType.value;
  }

  get planObserver(): Observable<Plan> {
    return this._plan;
  }

  setNetworkType(networkType: NetworkType): void {
    this._networkType.next(networkType);
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

  closeOverlay(): void {
    this.overlay.setPosition(undefined);
  }

}
