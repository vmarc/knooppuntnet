import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';
import {Bounds} from '../../kpn/api/common/bounds';
import {LongDistanceRouteChangesPage} from '../../kpn/api/common/longdistance/long-distance-route-changes-page';
import {LongDistanceRouteDetailsPage} from '../../kpn/api/common/longdistance/long-distance-route-details-page';
import {LongDistanceRouteMapPage} from '../../kpn/api/common/longdistance/long-distance-route-map-page';
import {LongDistanceRoutesPage} from '../../kpn/api/common/longdistance/long-distance-routes-page';
import {ApiResponse} from '../../kpn/api/custom/api-response';

export const actionLongDistanceRoutesLoaded = createAction(
  '[Long distance] Routes loaded',
  props<{ response: ApiResponse<LongDistanceRoutesPage> }>()
);

export const actionLongDistanceRouteDetailsLoaded = createAction(
  '[Long distance] Route details loaded',
  props<{ response: ApiResponse<LongDistanceRouteDetailsPage> }>()
);

export const actionLongDistanceRouteMapLoaded = createAction(
  '[Long distance] Route map loaded',
  props<{ response: ApiResponse<LongDistanceRouteMapPage> }>()
);

export const actionLongDistanceRouteChangesLoaded = createAction(
  '[Long distance] Route changes loaded',
  props<{ response: ApiResponse<LongDistanceRouteChangesPage> }>()
);

export const actionLongDistanceRouteMapFocus = createAction(
  '[Long distance] Focus',
  props<{ segmentId: number, bounds: Bounds }>()
);
