import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {LatLonImpl} from "../../../kpn/api/common/lat-lon-impl";
import {PlanNode} from "./plan-node";
import {PlanSegment} from "./plan-segment";

export class PlanRoute {

  constructor(readonly source: PlanNode,
              readonly sink: PlanNode,
              readonly meters: number,
              readonly segments: List<PlanSegment>,
              readonly streets: List<string>) {
  }

  latLons(): List<LatLonImpl> {
    return List([this.source.latLon]).concat(this.segments.flatMap(segment => segment.fragments.map(fragment => fragment.latLon)));
  }

  coordinates(): List<Coordinate> {
    return List([this.source.coordinate]).concat(this.segments.flatMap(segment => segment.fragments.map(fragment => fragment.coordinate)));
  }

}
