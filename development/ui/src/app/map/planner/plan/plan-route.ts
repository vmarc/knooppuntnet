import {List} from "immutable";
import Coordinate from "ol/coordinate";
import {PlanNode} from "./plan-node";
import {PlanSegment} from "./plan-segment";

export class PlanRoute {

  constructor(readonly source: PlanNode,
              readonly sink: PlanNode,
              readonly meters: number,
              readonly segments: List<PlanSegment>,
              readonly streets: List<string>) {
  }

  coordinates(): List<Coordinate> {
    return List([this.source.coordinate]).concat(this.segments.flatMap(segment => segment.fragments.map(fragment => fragment.coordinate)));
  }

}
