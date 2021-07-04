import { TrackPathKey } from '@api/common/common/track-path-key';
import { FeatureLike } from 'ol/Feature';

export class RouteFeature {
  constructor(
    readonly routeId: number,
    readonly pathId: number,
    readonly routeName: string,
    readonly oneWay: boolean,
    readonly proposed: boolean,
    readonly feature: FeatureLike
  ) {}

  toTrackPathKey(): TrackPathKey {
    return {
      routeId: this.routeId,
      pathId: this.pathId,
    };
  }
}
