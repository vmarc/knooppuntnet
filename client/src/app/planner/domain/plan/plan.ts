import { Bounds } from '@api/common';
import { LatLonImpl } from '@api/common';
import { PlanNode } from '@api/common/planner';
import { Util } from '@app/components/shared';
import { List } from 'immutable';
import { PlanFlag } from './plan-flag';
import { PlanLeg } from './plan-leg';
import { PlanUtil } from './plan-util';

export class Plan {
  static readonly empty = new Plan(null, null, List());

  constructor(
    readonly sourceNode: PlanNode,
    readonly sourceFlag: PlanFlag,
    readonly legs: List<PlanLeg>
  ) {}

  withLegs(legs: List<PlanLeg>): Plan {
    return new Plan(this.sourceNode, this.sourceFlag, legs);
  }

  sinkNode(): PlanNode {
    const lastLeg = this.legs.last(null);
    if (lastLeg) {
      return lastLeg.sinkNode;
    }
    return this.sourceNode;
  }

  sinkFlag(): PlanFlag {
    const lastLeg = this.legs.last(null);
    if (lastLeg) {
      return lastLeg.sinkFlag;
    }
    return null;
  }

  meters(): number {
    return Util.sum(this.legs.map((l) => l.meters()));
  }

  cumulativeMetersLeg(legIndex: number): number {
    if (legIndex < this.legs.size) {
      return Util.sum(this.legs.slice(0, legIndex + 1).map((l) => l.meters()));
    }
    return 0;
  }

  cumulativeKmLeg(legIndex: number): string {
    const meters = this.cumulativeMetersLeg(legIndex);
    const km = Math.round(meters / 100) / 10;
    const kmString = parseFloat(km.toFixed(1));
    return kmString + ' km';
  }

  bounds(): Bounds {
    if (this.sourceNode === null) {
      return null;
    }
    const latLons: List<LatLonImpl> = List([this.sourceNode.latLon]).concat(
      this.legs.flatMap((leg) =>
        List([leg.sourceNode.latLon]).concat(
          leg.routes.flatMap((route) => PlanUtil.planRouteLatLons(route))
        )
      )
    );

    const lats = latLons.map((latLon) => +latLon.latitude);
    const lons = latLons.map((latLon) => +latLon.longitude);
    const minLat = lats.min();
    const minLon = lons.min();
    const maxLat = lats.max();
    const maxLon = lons.max();
    return {
      minLat,
      minLon,
      maxLat,
      maxLon,
    };
  }

  unpavedPercentage(): string {
    const distances: List<number> = this.legs.flatMap((leg) =>
      leg.routes.flatMap((route) =>
        route.segments
          .filter((segment) => segment.surface === 'unpaved')
          .map((segment) => segment.meters)
      )
    );
    const unpavedMeters = Util.sum(distances);
    const percentage = Math.round((100 * unpavedMeters) / this.meters());
    return `${percentage}%`;
  }
}
