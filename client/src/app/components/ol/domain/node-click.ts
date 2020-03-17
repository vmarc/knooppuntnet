import {Coordinate} from "ol/coordinate";
import {NodeFeature} from "../../../map/planner/features/node-feature";

export class NodeClick {
  constructor(readonly coordinate: Coordinate,
              readonly node: NodeFeature) {
  }
}
