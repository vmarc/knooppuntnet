import {PlannerEngine} from "./planner-engine";
import {PlannerContext} from "./planner-context";

export class PlannerEngineImpl implements PlannerEngine {

  // construct layers here???

  constructor(private context: PlannerContext) {
  }

  public nodeSelected(): void {
    // if (first node) {
    //   const command = new PlannerCommandAddStartNode(...);
    //   context.addCommand(command)
    // }
    // else {
    //   if (route leg info already available in cache from previous request) {
    //     this.updateLegInformation(...)
    //   }
    //   else {
    //     // make request to server to get leg information ==> determine id that will be given to the leg (routeLegId)
    //     // execute legInformationReceived() when answer is received
    //   }
    //
    //   const command = new PlannerCommandAddLeg(... routeLegId ...);
    //   context.addCommand(command)
    // }
  }

  public undo(): void {
    const command = this.context.commandStack.undo();
    command.undo(this.context);
  }

  private legInformationReceived(): void {
    this.updateLegInformation();
  }

  private updateLegInformation(): void {
    // add routeLeg to leg information
    // add routeLeg to routeLayer, so that leg gets displayed on the map
    // add routeLeg information (e.g. distance) to node list component in sidebar (also update total distance)
  }

}
