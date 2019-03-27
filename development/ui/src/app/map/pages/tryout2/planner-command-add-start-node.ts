import {PlannerCommand} from "./planner-command";
import {PlannerContext} from "./planner-context";

export class PlannerCommandAddStartNode implements PlannerCommand {

  constructor(/* node id + coordinate + etc? */) {
  }

  public do(context: PlannerContext) {
    // - add to list of nodes
    // - add Feature with flag
    //   - remove old flag Feature when selecting next end-node (previous endnode becomes via-point)
    // - make sure that the node list component in sidebar is updated

    // if (legInfo already available in cache (which should be the case for redo()) ) { put it on the map }
  }

  public undo(context: PlannerContext) {
    // - first simplistic implementation
    //   - remove top node from list of nodes
  }

}
