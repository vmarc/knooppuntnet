import {PlannerContext} from "./planner-context";

export interface PlannerCommand {
  do(context: PlannerContext);
  undo(context: PlannerContext);
}
