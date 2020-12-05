import {Bounds} from '../../kpn/api/common/bounds';
import {LongDistanceRouteChangesPage} from '../../kpn/api/common/longdistance/long-distance-route-changes-page';
import {LongDistanceRouteDetailsPage} from '../../kpn/api/common/longdistance/long-distance-route-details-page';
import {LongDistanceRouteMapPage} from '../../kpn/api/common/longdistance/long-distance-route-map-page';
import {LongDistanceRoutesPage} from '../../kpn/api/common/longdistance/long-distance-routes-page';
import {ApiResponse} from '../../kpn/api/custom/api-response';

export const initialState: LongDistanceState = {
  routeId: 0,
  routeName: '',
  routes: null,
  details: null,
  map: null,
  changes: null,
  mapFocusNokSegmentId: null,
  mapFocus: null
};

export interface LongDistanceState {
  routeId: number;
  routeName: string;
  routes: ApiResponse<LongDistanceRoutesPage>;
  details: ApiResponse<LongDistanceRouteDetailsPage>;
  map: ApiResponse<LongDistanceRouteMapPage>;
  changes: ApiResponse<LongDistanceRouteChangesPage>;
  mapFocusNokSegmentId: number;
  mapFocus: Bounds;
}
