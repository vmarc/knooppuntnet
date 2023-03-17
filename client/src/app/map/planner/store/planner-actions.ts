import { MapPosition } from '@app/components/ol/domain/map-position';
import { MapMode } from '@app/components/ol/services/map-mode';
import { PlannerState } from '@app/map/planner/store/planner-state';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionPlannerPageInit = createAction('[Planner] Init');

export const actionPlannerLoad = createAction(
  '[Planner] Load',
  props<PlannerState>()
);

export const actionPlannerLoaded = createAction('[Planner] Loaded');

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
  props<MapPosition>()
);
