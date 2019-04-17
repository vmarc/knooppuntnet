import {List} from "immutable";
import Coordinate from "ol/coordinate";
import {PlanNode} from "./plan-node";
import {PlanSegment} from "./plan-segment";

export class PlanRoute {

  constructor(public readonly source: PlanNode,
              public readonly sink: PlanNode,
              public readonly meters: number,
              public readonly segments: List<PlanSegment>,
              public readonly streets: List<string>) {
  }

  coordinates(): List<Coordinate> {
    return List([this.source.coordinate]).concat(this.segments.flatMap(segment => segment.fragments.map(fragment => fragment.coordinate)));
  }

}
