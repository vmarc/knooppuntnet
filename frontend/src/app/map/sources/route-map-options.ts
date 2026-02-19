import { RouteType } from '@api/common/route-type';
import { MapMode } from '@app/mapold/domain/map-mode';

export interface RouteMapOptions {
  readonly routeType: RouteType;
  readonly mapMode: MapMode;
  readonly scopeInternational: boolean;
  readonly scopeNational: boolean;
  readonly scopeRegional: boolean;
  readonly scopeLocal: boolean;
  readonly nodeRoutes: boolean;
  readonly routeIds: ReadonlyArray<number>;
  readonly nodeIds: ReadonlyArray<number>;
}
