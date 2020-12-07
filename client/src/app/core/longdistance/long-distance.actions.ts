import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';
import {Bounds} from '../../kpn/api/common/bounds';
import {BoundsI} from '../../kpn/api/common/bounds-i';
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

export const actionLongDistanceRouteMapMode = createAction(
  '[Long distance] Map mode',
  props<{ mode: string }>()
);

export const actionLongDistanceRouteMapFocus = createAction(
  '[Long distance] Focus',
  props<{ bounds: BoundsI }>()
);

export const actionLongDistanceRouteMapGpxVisible = createAction(
  '[Long distance] Map gpx visible',
  props<{ visible: boolean }>()
);

export const actionLongDistanceRouteMapGpxOkVisible = createAction(
  '[Long distance] Map gpx ok visible',
  props<{ visible: boolean }>()
);

export const actionLongDistanceRouteMapGpxNokVisible = createAction(
  '[Long distance] Map gpx nok visible',
  props<{ visible: boolean }>()
);

export const actionLongDistanceRouteMapOsmRelationVisible = createAction(
  '[Long distance] Map osm relation visible',
  props<{ visible: boolean }>()
);
