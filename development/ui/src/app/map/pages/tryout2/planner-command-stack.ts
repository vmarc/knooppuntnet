import {PlannerCommand} from "./planner-command";

export interface PlannerCommandStack {

  commandCount: number;

  size: number;

  push(command: PlannerCommand): void;

  undo(): PlannerCommand;

  redo(): PlannerCommand;

  canUndo: boolean;

  canRedo: boolean;

}
