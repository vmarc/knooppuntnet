import { NetworkType } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { MapLayerState } from '@app/ol/domain';
import { MapMode } from '@app/ol/services';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';
import { PlannerState } from './planner-state';

export const actionPlannerInit = createAction('[Planner] Init');

export const actionPlannerLoad = createAction('[Planner] Load', props<{ state: PlannerState }>());

export const actionPlannerMapViewInit = createAction('[Planner] MapViewInit');

export const actionPlannerMapFinalized = createAction(
  '[Planner] Map finalized',
  props<{ position: MapPosition; layerStates: MapLayerState[] }>()
);

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

export const actionPlannerLayerStates = createAction(
  '[Planner] LayerStates',
  props<{ layerStates: MapLayerState[] }>()
);

export const actionPlannerPoisVisible = createAction(
  '[Planner] Pois visible',
  props<{ visible: boolean }>()
);

export const actionPlannerPoiGroupVisible = createAction(
  '[Planner] Poi group visible',
  props<{ groupName: string; visible: boolean }>()
);
