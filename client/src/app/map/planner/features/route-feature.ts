import {FeatureLike} from 'ol/Feature';
import {TrackPathKey} from '../../../kpn/api/common/common/track-path-key';

export class RouteFeature {
  constructor(readonly routeId: number,
              readonly pathId: number,
              readonly routeName: string,
              readonly oneWay: boolean,
              readonly feature: FeatureLike) {
  }

  toTrackPathKey(): TrackPathKey {
    return new TrackPathKey(this.routeId, this.pathId);
  }

}
