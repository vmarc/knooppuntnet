import {List} from "immutable";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {LegEndNode} from "../../../kpn/api/common/planner/leg-end-node";
import {LegEndRoute} from "../../../kpn/api/common/planner/leg-end-route";
import {Plan} from "./plan";

export class PlanUtil {

  static toUrlString(plan: Plan): string {

    let legEnds: List<LegEnd> = List();

    if (plan.sourceNode !== null) {
      legEnds = legEnds.push(PlanUtil.legEndNode(+plan.sourceNode.nodeId));
    }

    plan.legs.forEach(leg => {
      legEnds = legEnds.push(leg.sink);
    });

    return legEnds.map(legEnd => PlanUtil.encodedLegEndKey(legEnd)).join("-");
  }

  static toNodeIds(planUrlString: string): List<string> {
    const nodeIdsRadix36: List<string> = List(planUrlString.split("-"));
    return nodeIdsRadix36.map(nodeId => parseInt(nodeId, 36).toString());
  }

  static distinctColours(colours: List<string>): List<string> {
    return colours.reduce((unique, colour) => {
      if (unique.isEmpty() || (unique.last() !== colour)) {
        return unique.push(colour);
      }
      return unique;
    }, List<string>());
  }

  static key(source: LegEnd, sink: LegEnd): string {
    return PlanUtil.legEndKey(source) + ">" + PlanUtil.legEndKey(sink);
  }

  static legEndNode(nodeId: number): LegEnd {
    return new LegEnd(new LegEndNode(nodeId), null);
  }

  static legEndRoute(routeId: number, pathId: number): LegEnd {
    return new LegEnd(null, new LegEndRoute(routeId, pathId));
  }

  static legEndKey(legEnd: LegEnd): string {
    if (legEnd.node !== null) {
      return legEnd.node.nodeId.toString();
    }
    if (legEnd.route !== null) {
      return legEnd.route.routeId.toString() + "." + legEnd.route.pathId.toString();
    }
    return "";
  }

  static encodedLegEndKey(legEnd: LegEnd): string {
    if (legEnd.node !== null) {
      return legEnd.node.nodeId.toString(36);
    }
    if (legEnd.route !== null) {
      return legEnd.route.routeId.toString(36) + "." + legEnd.route.pathId.toString(36);
    }
    return "";
  }

}
