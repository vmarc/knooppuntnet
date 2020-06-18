import {List} from "immutable";
import {BehaviorSubject, Observable} from "rxjs";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerCommand} from "../commands/planner-command";
import {PlannerCommandStack} from "../commands/planner-command-stack";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanNode} from "../plan/plan-node";
import {PlannerCursor} from "./planner-cursor";
import {PlannerElasticBand} from "./planner-elastic-band";
import {PlannerHighlightLayer} from "./planner-highlight-layer";
import {PlannerLegRepository} from "./planner-leg-repository";
import {PlannerOverlay} from "./planner-overlay";
import {PlannerRouteLayer} from "./planner-route-layer";

export class NetworkTypeData {
  constructor(public plan: Plan,
              public commandStack: PlannerCommandStack) {
  }
}

export class PlannerContext {

  private networkTypeMap: Map<NetworkType, NetworkTypeData> = new Map();

  private _networkType$ = new BehaviorSubject<NetworkType>(null);
  networkType$: Observable<NetworkType> = this._networkType$.asObservable();
  private _plan$ = new BehaviorSubject<Plan>(Plan.empty());
  plan$: Observable<Plan> = this._plan$.asObservable();
  private _commandStack$ = new BehaviorSubject<PlannerCommandStack>(new PlannerCommandStack());

  constructor(readonly routeLayer: PlannerRouteLayer,
              readonly cursor: PlannerCursor,
              readonly elasticBand: PlannerElasticBand,
              readonly highlightLayer: PlannerHighlightLayer,
              readonly legRepository: PlannerLegRepository,
              readonly legs: PlanLegCache,
              readonly overlay: PlannerOverlay) {
  }

  get networkType(): NetworkType {
    return this._networkType$.value;
  }

  get plan(): Plan {
    return this._plan$.value;
  }

  get commandStack(): PlannerCommandStack {
    return this._commandStack$.value;
  }

  nextNetworkType(networkType: NetworkType): void {
    const oldNetworkType = this._networkType$.value;
    if (oldNetworkType) {
      const oldPlan = this._plan$.value;
      const oldCommandStack = this._commandStack$.value;
      this.routeLayer.removePlan(oldPlan);
      const data = new NetworkTypeData(oldPlan, oldCommandStack);
      this.networkTypeMap = this.networkTypeMap.set(oldNetworkType, data);
    }
    const existingData = this.networkTypeMap.get(networkType);
    if (existingData) {
      this.routeLayer.addPlan(existingData.plan);
      this._plan$.next(existingData.plan);
      this._commandStack$.next(existingData.commandStack);
    } else {
      this._plan$.next(Plan.empty());
      this._commandStack$.next(new PlannerCommandStack());
    }
    this._networkType$.next(networkType);
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
    this._plan$.next(plan);
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
    this.overlay.setPosition(undefined, 0);
  }

  buildLeg(legId: string, source: PlanNode, sink: PlanNode): PlanLeg {

    const cachedLeg = this.legs.get(source.nodeId, sink.nodeId);
    if (cachedLeg) {
      const planLeg = new PlanLeg(legId, source, sink, cachedLeg.meters, cachedLeg.routes);
      this.legs.add(planLeg);
      return planLeg;
    }

    this.legRepository.planLeg(this.networkType.name, legId, source, sink).subscribe(planLeg => {
      if (planLeg) {
        this.legs.add(planLeg);
        this.updatePlanLeg(planLeg);
      }
    });

    const leg = new PlanLeg(legId, source, sink, 0, List());
    this.legs.add(leg);
    return leg;
  }
}
