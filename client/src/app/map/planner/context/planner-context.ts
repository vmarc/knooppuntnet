import {List} from "immutable";
import {BehaviorSubject, Observable} from "rxjs";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerCommand} from "../commands/planner-command";
import {PlannerCommandStack} from "../commands/planner-command-stack";
import {FeatureId} from "../features/feature-id";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCursor} from "./planner-cursor";
import {PlannerElasticBand} from "./planner-elastic-band";
import {PlannerHighlightLayer} from "./planner-highlight-layer";
import {PlannerLegRepository} from "./planner-leg-repository";
import {PlannerMarkerLayer} from "./planner-marker-layer";
import {PlannerOverlay} from "./planner-overlay";
import {PlannerRouteLayer} from "./planner-route-layer";

export class NetworkTypeData {
  constructor(public plan: Plan,
              public commandStack: PlannerCommandStack) {
  }
}

export class PlannerContext {

  plan$: Observable<Plan>;
  networkType$: Observable<NetworkType>;

  private _plan$: BehaviorSubject<Plan>;
  private _networkType$: BehaviorSubject<NetworkType>;
  private _commandStack$: BehaviorSubject<PlannerCommandStack>;
  private networkTypeMap: Map<NetworkType, NetworkTypeData> = new Map();

  constructor(readonly routeLayer: PlannerRouteLayer,
              readonly markerLayer: PlannerMarkerLayer,
              readonly cursor: PlannerCursor,
              readonly elasticBand: PlannerElasticBand,
              readonly highlightLayer: PlannerHighlightLayer,
              readonly legRepository: PlannerLegRepository,
              readonly legs: PlanLegCache,
              readonly overlay: PlannerOverlay) {
    this._plan$ = new BehaviorSubject<Plan>(Plan.empty);
    this.plan$ = this._plan$.asObservable();
    this._networkType$ = new BehaviorSubject<NetworkType>(null);
    this.networkType$ = this._networkType$.asObservable();
    this._commandStack$ = new BehaviorSubject<PlannerCommandStack>(new PlannerCommandStack());
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
      this.markerLayer.removePlan(oldPlan);
      const data = new NetworkTypeData(oldPlan, oldCommandStack);
      this.networkTypeMap = this.networkTypeMap.set(oldNetworkType, data);
    }
    const existingData = this.networkTypeMap.get(networkType);
    if (existingData) {
      this.routeLayer.addPlan(existingData.plan);
      this.markerLayer.addPlan(existingData.plan);
      this._plan$.next(existingData.plan);
      this._commandStack$.next(existingData.commandStack);
    } else {
      this._plan$.next(Plan.empty);
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
    const newLegs = this.plan.legs.map(leg => leg.featureId === newLeg.featureId ? newLeg : leg);
    const newPlan = new Plan(this.plan.sourceNode, this.plan.sourceFlag, newLegs);
    this.updatePlan(newPlan);
    this.routeLayer.addPlanLeg(newLeg);
  }

  closeOverlay(): void {
    this.overlay.setPosition(undefined, 0);
  }

  buildLeg(source: LegEnd, sink: LegEnd, sourceNode: PlanNode, sinkNode: PlanNode, sinkFlagType: PlanFlagType): PlanLeg {

    const legKey = PlanUtil.key(source, sink);
    // const cachedLeg = this.legs.get(legKey);
    // if (cachedLeg) {
    //   const planLeg = new PlanLeg(
    //     legFeatureId,
    //     legKey,
    //     cachedLeg.source,
    //     cachedLeg.sink,
    //     cachedLeg.sourceNode, // TODO PLAN should have PlanNode with new featureId ?
    //     cachedLeg.sinkNode, // TODO PLAN should have PlanNode with new featureId ?
    //     cachedLeg.meters,
    //     cachedLeg.routes // TODO PLAN PlanNode's in routes should alse have new featureId ?
    //   );
    //   this.legs.add(planLeg);
    //   return planLeg;
    // }

    const legFeatureId = FeatureId.next();

    let sinkFlag: PlanFlag = null;
    let viaFlag: PlanFlag = null;

    if (sink.node !== null) {
      sinkFlag = new PlanFlag(sinkFlagType, FeatureId.next(), sinkNode.coordinate);
    }
    if (sink.route !== null) { // TODO PLAN still need a way to get sinkNode coordinate --> should be coordinate that was clicked
      viaFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), sinkNode.coordinate);
      sinkFlag = new PlanFlag(PlanFlagType.Invisible, FeatureId.next(), sinkNode.coordinate);
    }

    const leg = new PlanLeg(legFeatureId, legKey, source, sink, sinkFlag, viaFlag, List());
    this.legs.add(leg);

    this.legRepository.planLeg(this.networkType, source, sink).subscribe(planLegDetail => {
      if (planLegDetail) {
        let updatedSinkFlag = sinkFlag;
        if (sink.route !== null) {
          const newSinkNode = planLegDetail.routes.last(null).sinkNode;
          updatedSinkFlag = PlanFlag.end(sinkFlag.featureId, newSinkNode.coordinate);
          this.markerLayer.updateFlag(updatedSinkFlag);
        }
        const updatedLeg = new PlanLeg(legFeatureId, legKey, source, sink, updatedSinkFlag, viaFlag, planLegDetail.routes);
        this.legs.add(updatedLeg);
        this.updatePlanLeg(updatedLeg);
      }
    });

    return leg;
  }
}
