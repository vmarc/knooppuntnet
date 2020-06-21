import {PlannerContext} from "../context/planner-context";

export interface PlannerCommand {

  do(context: PlannerContext): void;

  undo(context: PlannerContext): void;

}
