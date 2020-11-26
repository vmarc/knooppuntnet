import {NodeState} from "./node/node.state";
import {RouteState} from "./route/route.state";

export interface AnalysisState {
  node: NodeState;
  route: RouteState;
}
