import { NetworkType } from '@api/custom/network-type';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { MapMode } from '@app/components/ol/services/map-mode';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';
import { PlannerState } from './planner-state';

export const actionPlannerInit = createAction('[Planner] Init');

export const actionPlannerLoad = createAction(
  '[Planner] Load',
  props<PlannerState>()
);

export const actionPlannerMapViewInit = createAction('[Planner] MapViewInit');

export const actionPlannerNetworkType = createAction(
  '[Planner] NetworkType',
  props<{ networkType: NetworkType }>()
);

export const actionPlannerMapMode = createAction(
  '[Planner] MapMode',
  props<{ mapMode: MapMode }>()
);

export const actionPlannerResultMode = createAction(
  '[Planner] ResultMode',
  props<{ resultMode: string }>()
);

export const actionPlannerPosition = createAction(
  '[Planner] Position',
  props<{ mapPosition: MapPosition }>()
);

export const actionPlannerLayerVisible = createAction(
  '[Planner] Layer visible',
  props<{ layerName: string; visible: boolean }>()
);

export const actionPlannerPoisEnabled = createAction(
  '[Planner] Pois enabled',
  props<{ enabled: boolean }>()
);

export const actionPlannerPoiGroupVisible = createAction(
  '[Planner] Poi group visible',
  props<{ groupName: string; visible: boolean }>()
);
