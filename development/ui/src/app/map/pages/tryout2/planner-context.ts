import Coordinate from 'ol/View';
import {Observable} from "rxjs";
import {PlannerMode} from "./planner-mode";
import {List} from "immutable";
import {Plan} from "./plan/plan";
import {PlanLegCache} from "./plan/plan-leg-cache";
import {PlanLegFragment} from "./plan/plan-leg-fragment";
import Feature from 'ol/Feature';
import {PlannerCommand} from "./commands/planner-command";

export interface PlannerContext {

  mode: Observable<PlannerMode>;
  planObserver: Observable<Plan>;
  plan: Plan;
  legCache: PlanLegCache;

  execute(command: PlannerCommand): void;

  canUndo(): boolean;

  canRedo(): boolean;

  undo(): void;

  redo(): void;

  updatePlan(plan: Plan): void;

  updatePlanLeg(legId: string, fragments: List<PlanLegFragment>): void;

  setCrosshairVisible(visible: boolean): void;

  setCrosshairPosition(coordinate: Coordinate): void;

  setCursorStyle(style: string): void;

  setElasticBand(anchor1: Coordinate, anchor2: Coordinate, coordinate: Coordinate): void;

  setElasticBandInvisible(): void;

  setElasticBandPosition(coordinate: Coordinate): void;

  addStartNodeFlag(nodeId: string, coordinate: Coordinate): Feature;

  addViaNodeFlag(legId: string, nodeId: string, coordinate: Coordinate): Feature;

  removeStartNodeFlag(nodeId: string): void;

  removeViaNodeFlag(legId: string, nodeId: string): void;

  addRouteLeg(legId: string, coordinates: List<Coordinate>): void;

  removeRouteLeg(legId: string): void;

}
