import {PlannerContext} from "../context/planner-context";

export interface PlannerCommand {

  do(context: PlannerContext);

  undo(context: PlannerContext);
}
