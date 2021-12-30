import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { RouteChangesPage } from '@api/common/route/route-changes-page';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { RouteMapPage } from '@api/common/route/route-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { NetworkType } from '@api/custom/network-type';

export const initialState: RouteState = {
  routeId: '',
  routeName: '',
  networkType: null,
  changeCount: 0,
  detailsPage: null,
  mapPage: null,
  changesPage: null,
  changesParameters: null,
};

export interface RouteState {
  routeId: string;
  routeName: string;
  networkType: NetworkType;
  changeCount: number;
  detailsPage: ApiResponse<RouteDetailsPage>;
  mapPage: ApiResponse<RouteMapPage>;
  changesPage: ApiResponse<RouteChangesPage>;
  changesParameters: ChangesParameters;
}

export const routeFeatureKey = 'route';
