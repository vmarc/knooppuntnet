import {RouteChangesPage} from '@api/common/route/route-changes-page';
import {RouteDetailsPage} from '@api/common/route/route-details-page';
import {RouteMapPage} from '@api/common/route/route-map-page';
import {ApiResponse} from '@api/custom/api-response';
import {AppState} from '../../../core/core.state';

export const initialState: RouteState = {
  routeId: '',
  routeName: '',
  changeCount: 0,
  detailsPage: null,
  mapPage: null,
  changesPage: null
};

export interface RouteState {
  routeId: string;
  routeName: string;
  changeCount: number;
  detailsPage: ApiResponse<RouteDetailsPage>;
  mapPage: ApiResponse<RouteMapPage>;
  changesPage: ApiResponse<RouteChangesPage>;
}

export const routeFeatureKey = 'route';

export interface RouteRootState extends AppState {
  [routeFeatureKey]: RouteState;
}
