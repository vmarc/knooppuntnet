import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {LatLonImpl} from "../../../kpn/api/common/lat-lon-impl";
import {ViaRoute} from "../../../kpn/api/common/planner/via-route";
import {PlanNode} from "./plan-node";
import {PlanRoute} from "./plan-route";

export class PlanLeg {

  constructor(readonly featureId: string,
              readonly source: PlanNode,
              readonly sink: PlanNode,
              readonly viaRoute: ViaRoute,
              readonly meters: number,
              readonly routes: List<PlanRoute>) {
  }

  latLons(): List<LatLonImpl> {
    return this.routes.flatMap(route => route.latLons());
  }

  coordinates(): List<Coordinate> {
    return this.routes.flatMap(route => route.coordinates());
  }

}
