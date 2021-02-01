import {LegEnd} from '@api/common/planner/leg-end';
import {NetworkType} from '@api/custom/network-type';
import {BehaviorSubject, Observable} from 'rxjs';
import {Subject} from 'rxjs';
import {tap} from 'rxjs/operators';
import {PlannerCommand} from '../commands/planner-command';
import {PlannerCommandStack} from '../commands/planner-command-stack';
import {PlannerDragFlag} from '../interaction/planner-drag-flag';
import {Plan} from '../plan/plan';
import {PlanLeg} from '../plan/plan-leg';
import {PlanLegData} from './plan-leg-data';
import {PlannerCursor} from './planner-cursor';
import {PlannerElasticBand} from './planner-elastic-band';
import {PlannerHighlighter} from './planner-highlighter';
import {PlannerLegRepository} from './planner-leg-repository';
import {PlannerMarkerLayer} from './planner-marker-layer';
import {PlannerOverlay} from './planner-overlay';
import {PlannerRouteLayer} from './planner-route-layer';

export class NetworkTypeData {
  constructor(public plan: Plan,
              public commandStack: PlannerCommandStack) {
  }
}

export class PlannerContext {

  plan$: Observable<Plan>;
  networkType$: Observable<NetworkType>;
  error$: Observable<Error>;

  private _plan$: BehaviorSubject<Plan>;
  private _networkType$: BehaviorSubject<NetworkType>;
  private _error$ = new Subject<Error>();
  private _commandStack$: BehaviorSubject<PlannerCommandStack>;
  private networkTypeMap: Map<NetworkType, NetworkTypeData> = new Map();

  constructor(readonly routeLayer: PlannerRouteLayer,
              readonly markerLayer: PlannerMarkerLayer,
              readonly cursor: PlannerCursor,
              readonly elasticBand: PlannerElasticBand,
              readonly highlighter: PlannerHighlighter,
              readonly legRepository: PlannerLegRepository,
              readonly overlay: PlannerOverlay) {
    this._plan$ = new BehaviorSubject<Plan>(Plan.empty);
    this.plan$ = this._plan$.asObservable();
    this._networkType$ = new BehaviorSubject<NetworkType>(null);
    this.networkType$ = this._networkType$.asObservable();
    this.error$ = this._error$.asObservable();
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

  fetchLeg(source: LegEnd, sink: LegEnd): Observable<PlanLegData> {
    this.cursor.setStyleWait();
    return this.legRepository.planLeg(this.networkType, source, sink).pipe(
      tap(() => {
        this.cursor.setStyleDefault();
        this.highlighter.reset();
      })
    );
  }

  debug(message: string): void {
    // console.log("PLANNER: " + message);
  }

  errorDialog(error: Error): void {
    this._error$.next(error);
  }

  resetDragFlag(dragFlag: PlannerDragFlag): void {
    this.markerLayer.updateFlagCoordinate(dragFlag.planFlag.featureId, dragFlag.oldNode.coordinate);
  }

}
