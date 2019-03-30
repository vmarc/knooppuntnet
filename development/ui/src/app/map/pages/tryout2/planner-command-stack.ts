import {Observable} from "rxjs";
import {PlannerCommand} from "./planner-command";

export interface PlannerCommandStack {

  commandCount: number;

  size: number;

  push(command: PlannerCommand): void;

  undo(): PlannerCommand;

  redo(): PlannerCommand;

  canUndo: Observable<boolean>;

  canRedo: Observable<boolean>;

  currentCanUndo: boolean;
  currentCanRedo: boolean;

}
