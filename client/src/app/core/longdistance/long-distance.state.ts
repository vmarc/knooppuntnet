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
  changes: null,
  map: null,
  mapMode: null,
  mapFocusNokSegmentId: null,
  mapFocus: null,
  mapGpxVisible: false,
  mapGpxOkVisible: false,
  mapGpxNokVisible: false,
  mapOsmRelationVisible: false
};

export interface LongDistanceState {
  routeId: number;
  routeName: string;
  routes: ApiResponse<LongDistanceRoutesPage>;
  details: ApiResponse<LongDistanceRouteDetailsPage>;
  changes: ApiResponse<LongDistanceRouteChangesPage>;
  map: ApiResponse<LongDistanceRouteMapPage>;
  mapMode: string,
  mapFocusNokSegmentId: number;
  mapFocus: Bounds;
  mapGpxVisible: boolean;
  mapGpxOkVisible: boolean;
  mapGpxNokVisible: boolean;
  mapOsmRelationVisible: boolean;
}
