import {RouteChangesPage} from '@api/common/route/route-changes-page';
import {RouteDetailsPage} from '@api/common/route/route-details-page';
import {RouteMapPage} from '@api/common/route/route-map-page';
import {ApiResponse} from '@api/custom/api-response';

export const initialState: RouteState = {
  routeId: '',
  routeName: '',
  changeCount: 0,
  details: null,
  map: null,
  changes: null
};

export interface RouteState {
  routeId: string;
  routeName: string;
  changeCount: number;
  details: ApiResponse<RouteDetailsPage>;
  map: ApiResponse<RouteMapPage>;
  changes: ApiResponse<RouteChangesPage>;
}
