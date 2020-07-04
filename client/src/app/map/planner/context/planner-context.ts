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
      const data = new NetworkTypeData(oldPlan, oldCommandStack);
      this.networkTypeMap = this.networkTypeMap.set(oldNetworkType, data);
    }
    const existingData = this.networkTypeMap.get(networkType);
    if (existingData) {
      this.routeLayer.addPlan(existingData.plan);
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

    console.log(["PLAN UPDATE", plan]);

  }

  updatePlanLeg(newLeg: PlanLeg) {
    const newLegs = this.plan.legs.map(leg => leg.featureId === newLeg.featureId ? newLeg : leg);

    let sourceNode = this.plan.sourceNode;
    let sourceFlag = this.plan.sourceFlag;

    if (newLeg.featureId === newLegs.get(0).featureId) {
      sourceNode = newLeg.sourceNode;
      sourceFlag = PlanFlag.start(sourceFlag.featureId, newLeg.sourceNode);
      this.routeLayer.addFlag(sourceFlag);
    }

    const newPlan = new Plan(sourceNode, sourceFlag, newLegs);
    this.updatePlan(newPlan);
    this.routeLayer.addPlanLeg(newLeg);
  }

  closeOverlay(): void {
    this.overlay.setPosition(undefined, 0);
  }

  oldBuildLeg(legId: string, sourceNode: PlanNode, sinkNode: PlanNode): PlanLeg {

    // const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    // const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    // const legKey = PlanUtil.key(source, sink);
    //
    // const cachedLeg = this.legs.get(legKey);
    // if (cachedLeg) {
    //   const planLeg = new PlanLeg(legId, legKey, source, sink, sourceNode, sinkNode, cachedLeg.meters, cachedLeg.routes);
    //   this.legs.add(planLeg);
    //   return planLeg;
    // }
    //
    // this.legRepository.planLeg(this.networkType, legId, source, sink).subscribe(planLeg => {
    //   if (planLeg) {
    //     this.legs.add(planLeg);
    //     this.updatePlanLeg(planLeg);
    //   }
    // });
    //
    // const leg = new PlanLeg(legId, legKey, source, sink, sourceNode, sinkNode, 0, List());
    // this.legs.add(leg);
    // return leg;
    return null;
  }

  buildLeg(source: LegEnd, sink: LegEnd, sourceNode: PlanNode, sinkNode: PlanNode, sinkFlagType: PlanFlagType): PlanLeg {

    const legKey = PlanUtil.key(source, sink);
    // const cachedLeg = this.legs.get(legFeatureId);
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
      sinkFlag = new PlanFlag(PlanFlagType.Invisble, FeatureId.next(), sinkNode.coordinate);
    }

    const leg = new PlanLeg(legFeatureId, legKey, source, sink, sinkFlag, viaFlag, List());
    this.legs.add(leg);

    this.legRepository.planLeg(this.networkType, source, sink).subscribe(planLegDetail => {
      if (planLegDetail) {
        const updatedLeg = new PlanLeg(legFeatureId, legKey, source, sink, sinkFlag, viaFlag, planLegDetail.routes);
        this.legs.add(updatedLeg);
        this.updatePlanLeg(updatedLeg);
        // TODO PLAN
        // if (source.route !== null) {
        //   this.routeLayer.updateFlagCoordinate(sourceNode.featureId, newSourceNode.coordinate);
        // }
        // if (sink.route !== null) {
        //   this.routeLayer.updateFlagCoordinate(sinkNode.featureId, newSinkNode.coordinate);
        // }
      }
    });

    return leg;
  }
}
