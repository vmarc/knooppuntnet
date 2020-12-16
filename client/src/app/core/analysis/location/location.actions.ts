import {LocationChangesPage} from '@api/common/location/location-changes-page';
import {LocationEditPage} from '@api/common/location/location-edit-page';
import {LocationFactsPage} from '@api/common/location/location-facts-page';
import {LocationMapPage} from '@api/common/location/location-map-page';
import {LocationNodesPage} from '@api/common/location/location-nodes-page';
import {LocationRoutesPage} from '@api/common/location/location-routes-page';
import {ApiResponse} from '@api/custom/api-response';
import {props} from '@ngrx/store';
import {createAction} from '@ngrx/store';

export const actionLocationNodesLoaded = createAction(
  '[Location] Nodes loaded',
  props<{ response: ApiResponse<LocationNodesPage> }>()
);

export const actionLocationRoutesLoaded = createAction(
  '[Location] Routes loaded',
  props<{ response: ApiResponse<LocationRoutesPage> }>()
);

export const actionLocationFactsLoaded = createAction(
  '[Location] Facts loaded',
  props<{ response: ApiResponse<LocationFactsPage> }>()
);

export const actionLocationMapLoaded = createAction(
  '[Location] Map loaded',
  props<{ response: ApiResponse<LocationMapPage> }>()
);

export const actionLocationChangesLoaded = createAction(
  '[Location] Changes loaded',
  props<{ response: ApiResponse<LocationChangesPage> }>()
);

export const actionLocationEditLoaded = createAction(
  '[Location] Edit loaded',
  props<{ response: ApiResponse<LocationEditPage> }>()
);
