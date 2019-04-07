import {PlannerContext} from "../interaction/planner-context";

export interface PlannerCommand {
  do(context: PlannerContext);

  undo(context: PlannerContext);
}
