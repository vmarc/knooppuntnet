import {List} from "immutable";
import {BehaviorSubject, Observable} from "rxjs";
import {Util} from "../../../components/shared/util";
import {LatLonImpl} from "../../../kpn/api/common/lat-lon-impl";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerCommand} from "../commands/planner-command";
import {PlannerCommandStack} from "../commands/planner-command-stack";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegBuilder} from "../plan/plan-leg-builder";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanNode} from "../plan/plan-node";
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
    this._plan$ = new BehaviorSubject<Plan>(Plan.empty());
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
    const newLegs = this.plan.legs.map(leg => leg.featureId === newLeg.featureId ? newLeg : leg);
    const newPlan = Plan.create(this.plan.sourceNode, newLegs);
    this.updatePlan(newPlan);
    this.routeLayer.addRouteLeg(newLeg);
  }

  closeOverlay(): void {
    this.overlay.setPosition(undefined, 0);
  }

  oldBuildLeg(legId: string, sourceNode: PlanNode, sinkNode: PlanNode): PlanLeg {

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const legKey = PlanUtil.key(source, sink);

    const cachedLeg = this.legs.get(legKey);
    if (cachedLeg) {
      const planLeg = new PlanLeg(legId, legKey, source, sink, sourceNode, sinkNode, cachedLeg.meters, cachedLeg.routes);
      this.legs.add(planLeg);
      return planLeg;
    }

    this.legRepository.planLeg(this.networkType, legId, source, sink).subscribe(routeLeg => {
      if (routeLeg) {
        const planLeg = PlanLegBuilder.toPlanLeg(source, sink, sourceNode, sinkNode, routeLeg);
        this.legs.add(planLeg);
        this.updatePlanLeg(planLeg);
      }
    });

    const leg = new PlanLeg(legId, legKey, source, sink, sourceNode, sinkNode, 0, List());
    this.legs.add(leg);
    return leg;
  }

  buildLeg(legId: string, source: LegEnd, sink: LegEnd, sourceNode: PlanNode, sinkNode: PlanNode): PlanLeg {

    const legKey = PlanUtil.key(source, sink);
    const cachedLeg = this.legs.get(legKey);
    if (cachedLeg) {
      const planLeg = new PlanLeg(
        legId,
        legKey,
        cachedLeg.source,
        cachedLeg.sink,
        cachedLeg.sourceNode,
        cachedLeg.sinkNode,
        cachedLeg.meters,
        cachedLeg.routes
      );
      this.legs.add(planLeg);
      return planLeg;
    }

    this.legRepository.planLeg(this.networkType, legId, source, sink).subscribe(routeLeg => {
      if (routeLeg) {

        let newSourceNode = sourceNode;
        if (source.route !== null) {
          const sourceRouteLegNode = routeLeg.routes.first(null).source;
          const newCoordinate = Util.toCoordinate(sourceRouteLegNode.lat, sourceRouteLegNode.lon);
          newSourceNode = new PlanNode(
            sourceNode.featureId,
            sourceRouteLegNode.nodeId,
            sourceRouteLegNode.nodeName,
            newCoordinate,
            new LatLonImpl(sourceRouteLegNode.lat, sourceRouteLegNode.lon)
          );
        }

        let newSinkNode = sinkNode;
        if (sink.route !== null) {
          const sinkRouteLegNode = routeLeg.routes.last(null).sink;
          const newCoordinate = Util.toCoordinate(sinkRouteLegNode.lat, sinkRouteLegNode.lon);
          newSinkNode = new PlanNode(
            sinkNode.featureId,
            sinkRouteLegNode.nodeId,
            sinkRouteLegNode.nodeName,
            newCoordinate,
            new LatLonImpl(sinkRouteLegNode.lat, sinkRouteLegNode.lon)
          );
        }

        const planLeg = PlanLegBuilder.toPlanLeg(source, sink, newSourceNode, newSinkNode, routeLeg);
        this.legs.add(planLeg);
        this.updatePlanLeg(planLeg);

        if (source.route !== null) {
          this.routeLayer.updateFlagCoordinate(sourceNode.featureId, newSourceNode.coordinate);
        }
        if (sink.route !== null) {
          this.routeLayer.updateFlagCoordinate(sinkNode.featureId, newSinkNode.coordinate);
        }
      }
    });

    const leg = new PlanLeg(legId, legKey, source, sink, sourceNode, sinkNode, 0, List());
    this.legs.add(leg);
    return leg;
  }
}
