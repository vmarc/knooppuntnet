import {List} from "immutable";
import {Bounds} from "../../../kpn/api/common/bounds";
import {LatLonImpl} from "../../../kpn/api/common/lat-lon-impl";
import {PlanLeg} from "./plan-leg";
import {PlanNode} from "./plan-node";

export class Plan {

  private static _empty: Plan = new Plan(null, List());

  private constructor(readonly sourceNode: PlanNode,
                      readonly legs: List<PlanLeg>) {
  }

  get sinkNode(): PlanNode {
    const lastLeg = this.legs.last(null);
    if (lastLeg) {
      return lastLeg.sinkNode;
    }
    return this.sourceNode;
  }

  static empty(): Plan {
    return Plan._empty;
  }

  static create(source: PlanNode, legs: List<PlanLeg>): Plan {
    return new Plan(source, legs);
  }

  meters(): number {
    return this.legs.map(l => l.meters).reduce((sum, current) => sum + current, 0);
  }

  cumulativeMetersLeg(legIndex: number): number {
    if (legIndex < this.legs.size) {
      return this.legs.slice(0, legIndex + 1).map(l => l.meters).reduce((sum, current) => sum + current, 0);
    }
    return 0;
  }

  cumulativeKmLeg(legIndex: number): string {
    const meters = this.cumulativeMetersLeg(legIndex);
    const km = Math.round(meters / 100) / 10;
    const kmString = parseFloat(km.toFixed(1));
    return kmString + " km";
  }

  bounds(): Bounds {
    if (this.sourceNode === null) {
      return null;
    }
    const latLons: List<LatLonImpl> = List([this.sourceNode.latLon]).concat(this.legs.flatMap(leg => List([leg.sourceNode.latLon]).concat(leg.latLons())));
    const lats = latLons.map(latLon => +latLon.latitude);
    const lons = latLons.map(latLon => +latLon.longitude);
    const minLat = lats.min();
    const minLon = lons.min();
    const maxLat = lats.max();
    const maxLon = lons.max();
    return new Bounds(minLat, minLon, maxLat, maxLon);
  }

  unpavedPercentage(): string {
    const distances: List<number> = this.legs.flatMap(leg => {
      return leg.routes.flatMap(route => {
        return route.segments.filter(segment => segment.surface === "unpaved").map(segment => segment.meters);
      });
    });
    const unpavedMeters = distances.reduce((sum, current) => sum + current, 0);
    const percentage = Math.round(100 * unpavedMeters / this.meters());
    return `${percentage}%`;
  }

}
