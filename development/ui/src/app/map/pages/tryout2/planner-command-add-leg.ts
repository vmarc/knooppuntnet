import {PlannerCommand} from "./planner-command";
import {PlannerContext} from "./planner-context";

export class PlannerCommandAddLeg implements PlannerCommand {

  constructor(/* node id + coordinate + routeLegId + etc. */) {
  }

  public do(context: PlannerContext) {
    // - remove old flag Feature from end-node (current last in nodelist) (previous endnode becomes via-point)
    // - add to list of nodes
    // - add Feature with end-flag
    // - make sure that the node list component in sidebar is updated
  }

  public undo(context: PlannerContext) {
    // - first simplistic implementation
    //   - remove top node from list of nodes
    //   - remove routeLeg from routeLayer if not start point
    //     - do not fail if the routeLeg was not added to layer by PlannerEngineImpl.legInformationReceived() yet
    //   - remove flag --> put back old end-flag if needed
  }

}
