import {List} from "immutable";
import {Coordinate} from 'ol/coordinate';
import Feature from 'ol/Feature';
import {Observable} from "rxjs";
import {PlannerCommand} from "./commands/planner-command";
import {Plan} from "./plan/plan";
import {PlanLegCache} from "./plan/plan-leg-cache";
import {PlanLegFragment} from "./plan/plan-leg-fragment";
import {PlannerMode} from "./planner-mode";

export interface PlannerContext {

  mode: Observable<PlannerMode>;
  planObserver: Observable<Plan>;
  legCache: PlanLegCache;

  plan(): Plan;

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

  updateFlagPosition(flagId: string, coordinate: Coordinate): void;

  setElasticBand(anchor1: Coordinate, anchor2: Coordinate, coordinate: Coordinate): void;

  setElasticBandInvisible(): void;

  setElasticBandPosition(coordinate: Coordinate): void;

  addStartNodeFlag(nodeId: string, coordinate: Coordinate): Feature;

  addViaNodeFlag(legId: string, nodeId: string, coordinate: Coordinate): Feature;

  removeStartNodeFlag(nodeId: string): void;

  removeViaNodeFlag(legId: string, nodeId: string): void;

  addRouteLeg(legId: string): void;

  removeRouteLeg(legId: string): void;

}
