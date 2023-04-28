import { ChangesParameters } from '@api/common/changes/filter';
import { RouteChangesPage } from '@api/common/route';
import { RouteDetailsPage } from '@api/common/route';
import { RouteMapPage } from '@api/common/route';
import { ApiResponse } from '@api/custom';
import { NetworkType } from '@api/custom';
import { MapPosition } from '@app/components/ol/domain';

export const initialState: RouteState = {
  routeId: '',
  routeName: '',
  networkType: null,
  changeCount: 0,
  detailsPage: null,
  mapPage: null,
  mapPositionFromUrl: null,
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
  mapPositionFromUrl: MapPosition;
  changesPage: ApiResponse<RouteChangesPage>;
  changesParameters: ChangesParameters;
}

export const routeFeatureKey = 'route';
