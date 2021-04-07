import { TrackPathKey } from '@api/common/common/track-path-key';
import { FeatureLike } from 'ol/Feature';

export class RouteFeature {
  constructor(
    readonly routeId: number,
    readonly pathId: number,
    readonly routeName: string,
    readonly oneWay: boolean,
    readonly feature: FeatureLike
  ) {}

  toTrackPathKey(): TrackPathKey {
    return new TrackPathKey(this.routeId, this.pathId);
  }
}
