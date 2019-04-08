import {List} from "immutable";
import {Coordinate} from 'ol/coordinate';
import {Feature} from 'ol/Feature';
import {BehaviorSubject, Observable} from "rxjs";
import {PlannerCommand} from "../commands/planner-command";
import {PlannerCommandStack} from "../commands/planner-command-stack";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanLegFragment} from "../plan/plan-leg-fragment";
import {PlannerCrosshair} from "./planner-crosshair";
import {PlannerElasticBand} from "./planner-elastic-band";
import {PlannerMode} from "./planner-mode";
import {PlannerRouteLayer} from "./planner-route-layer";

export class PlannerContext {

  private _mode = new BehaviorSubject<PlannerMode>(PlannerMode.Idle);
  private _plan = new BehaviorSubject<Plan>(Plan.empty());

  viewPort: HTMLElement;

  constructor(public readonly commandStack: PlannerCommandStack,
              public readonly routeLayer: PlannerRouteLayer,
              public readonly crosshair: PlannerCrosshair,
              public readonly elasticBand: PlannerElasticBand,
              public readonly legs: PlanLegCache) {
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

  setCursorStyle(style: string): void {
    this.viewPort.style.cursor = style;
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

  updatePlanLeg(legId: string, fragments: List<PlanLegFragment>) {
    const existingLeg = this.legs.getById(legId);
    const newLeg = new PlanLeg(legId, existingLeg.source, existingLeg.sink, fragments)


    const newLegs = this.plan.legs.map(leg => {
      if (leg.legId === legId) {
        return newLeg;
      }
      return leg;
    });
    const newPlan = new Plan(this.plan.source, newLegs);
    this.updatePlan(newPlan);

    this.routeLayer.addRouteLeg(newLeg);
  }

}
