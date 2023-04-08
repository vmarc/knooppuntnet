import { NetworkType } from '@api/custom/network-type';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { MapMode } from '@app/components/ol/services/map-mode';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';

export const actionPlannerInit = createAction('[Planner] Init');

export const actionPlannerLoad = createAction(
  '[Planner] Load',
  props<{ networkType: NetworkType; mapMode: MapMode; resultMode: string }>()
);

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

export const actionPlannerPoisEnabled = createAction(
  '[Planner] Pois enabled',
  props<{ enabled: boolean }>()
);

export const actionPlannerPoiGroupVisible = createAction(
  '[Planner] Poi group visible',
  props<{ groupName: string; visible: boolean }>()
);
