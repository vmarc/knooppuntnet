import {List} from "immutable";
import {Plan} from "./plan";

export class PlanUtil {

  static toUrlString(plan: Plan): string {

    let nodeIds: List<string> = List();

    if (plan.source !== null) {
      nodeIds = nodeIds.push(plan.source.nodeId);
    }

    plan.legs.forEach(leg => {
      nodeIds = nodeIds.push(leg.sink.nodeId);
    });

    return nodeIds.map(nodeId => (+nodeId).toString(36)).join("-");
  }

  static toNodeIds(planUrlString: string): List<string> {
    const nodeIdsRadix36: List<string> = List(planUrlString.split("-"));
    return nodeIdsRadix36.map(nodeId => parseInt(nodeId, 36).toString());
  }

}
