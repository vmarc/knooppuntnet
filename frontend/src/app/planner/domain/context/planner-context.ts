import { signal } from '@angular/core';
import { Signal } from '@angular/core';
import { LegEnd } from '@api/common/planner';
import { NetworkType } from '@api/custom';
import { Subscriptions } from '@app/util';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PlannerCommand } from '../commands/planner-command';
import { PlannerCommandStack } from '../commands/planner-command-stack';
import { PlannerDragFlag } from '../interaction/planner-drag-flag';
import { Plan } from '../plan/plan';
import { PlanLeg } from '../plan/plan-leg';
import { PlanLegData } from './plan-leg-data';
import { PlannerCursor } from './planner-cursor';
import { PlannerElasticBand } from './planner-elastic-band';
import { PlannerHighlighter } from './planner-highlighter';
import { PlannerLegRepository } from './planner-leg-repository';
import { PlannerMarkerLayer } from './planner-marker-layer';
import { PlannerPopup } from './planner-popup';
import { PlannerRouteLayer } from './planner-route-layer';

export class NetworkTypeData {
  constructor(
    public plan: Plan,
    public commandStack: PlannerCommandStack
  ) {}
}

export class PlannerContext {
  private readonly _plan = signal<Plan>(Plan.empty);
  private readonly _error = signal<Error | null>(null);
  private readonly _commandStack = signal<PlannerCommandStack>(new PlannerCommandStack());
  private networkTypeMap: Map<NetworkType, NetworkTypeData> = new Map();

  private subscriptions = new Subscriptions();

  readonly plan = this._plan.asReadonly();
  readonly error = this._error.asReadonly();
  readonly commandStack = this._commandStack.asReadonly();

  private networkType: NetworkType;

  constructor(
    readonly routeLayer: PlannerRouteLayer,
    readonly markerLayer: PlannerMarkerLayer,
    readonly cursor: PlannerCursor,
    readonly elasticBand: PlannerElasticBand,
    readonly highlighter: PlannerHighlighter,
    readonly legRepository: PlannerLegRepository,
    readonly plannerPopup: PlannerPopup,
    readonly planProposed: Signal<boolean>
  ) {}

  destroy(): void {
    this.subscriptions.unsubscribe();
  }

  setNetworkType(networkType: NetworkType): void {
    const oldNetworkType = this.networkType;
    this.networkType = networkType;
    if (oldNetworkType) {
      const oldPlan = this.plan();
      const oldCommandStack = this.commandStack();
      this.routeLayer.removePlan(oldPlan);
      this.markerLayer.removePlan(oldPlan);
      const data = new NetworkTypeData(oldPlan, oldCommandStack);
      this.networkTypeMap = this.networkTypeMap.set(oldNetworkType, data);
    }
    const existingData = this.networkTypeMap.get(networkType);
    if (existingData) {
      this.routeLayer.addPlan(existingData.plan);
      this.markerLayer.addPlan(existingData.plan);
      this._plan.set(existingData.plan);
      this._commandStack.set(existingData.commandStack);
    } else {
      this._plan.set(Plan.empty);
      this._commandStack.set(new PlannerCommandStack());
    }
  }

  execute(command: PlannerCommand): void {
    this.commandStack().push(command);
    command.do(this);
  }

  undo(): void {
    const command = this.commandStack().undo();
    command.undo(this);
  }

  redo(): void {
    const command = this.commandStack().redo();
    command.do(this);
  }

  updatePlan(plan: Plan) {
    this._plan.set(plan);
  }

  updatePlanLeg(newLeg: PlanLeg) {
    const newLegs = this.plan().legs.map((leg) =>
      leg.featureId === newLeg.featureId ? newLeg : leg
    );
    const newPlan = new Plan(this.plan().sourceNode, this.plan().sourceFlag, newLegs);
    this.updatePlan(newPlan);
    this.routeLayer.addPlanLeg(newLeg);
  }

  closeOverlay(): void {
    this.plannerPopup.reset();
  }

  fetchLeg(source: LegEnd, sink: LegEnd): Observable<PlanLegData> {
    this.cursor.setStyleWait();
    return this.legRepository.planLeg(this.networkType, source, sink, this.planProposed()).pipe(
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
    this._error.set(error);
  }

  resetDragFlag(dragFlag: PlannerDragFlag): void {
    this.markerLayer.updateFlagCoordinate(dragFlag.planFlag.featureId, dragFlag.oldNode.coordinate);
  }
}
