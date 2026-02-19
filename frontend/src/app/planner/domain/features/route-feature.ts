import { TrackPathKey } from '@api/common/common/track-path-key';
import { MapGeoJSONFeature } from 'maplibre-gl';

export class RouteFeature {
  constructor(
    readonly routeId: number,
    readonly pathId: number,
    readonly routeName: string,
    readonly oneWay: boolean,
    readonly proposed: boolean,
    readonly feature: MapGeoJSONFeature
  ) {}

  toTrackPathKey(): TrackPathKey {
    return {
      routeId: this.routeId,
      pathId: this.pathId,
    };
  }
}
