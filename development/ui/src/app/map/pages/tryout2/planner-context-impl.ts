import {PlannerContext} from "./planner-context";
import {BehaviorSubject, Observable} from "rxjs";
import {PlannerMode} from "./planner-mode";
import {PlannerRouteLayer} from "./planner-route-layer";
import {PlannerCrosshairLayer} from "./planner-crosshair-layer";
import {Plan} from "./plan";
import {PlannerCommandStack} from "./planner-command-stack";
import {List} from "immutable";

export class PlannerContextImpl implements PlannerContext {

  private mode_ = new BehaviorSubject<PlannerMode>(PlannerMode.Idle);

  private plan_ = new BehaviorSubject<Plan>(new Plan(List()));


  constructor(public commandStack: PlannerCommandStack,
              public routeLayer: PlannerRouteLayer,
              public crosshairLayer: PlannerCrosshairLayer) {
  }

  public get mode(): Observable<PlannerMode> {
    return this.mode_;
  }

  public get plan(): Observable<Plan> {
    return this.plan_;
  }

}
