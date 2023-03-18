import { createSelector } from '@ngrx/store';
import { createFeatureSelector } from '@ngrx/store';
import { PlannerState } from './planner-state';
import { plannerFeatureKey } from './planner-state';

export const selectPlannerState =
  createFeatureSelector<PlannerState>(plannerFeatureKey);

export const selectPlannerNetworkType = createSelector(
  selectPlannerState,
  (state) => state.networkType
);

export const selectPlannerMapPosition = createSelector(
  selectPlannerState,
  (state) => state.position
);

export const selectPlannerMapMode = createSelector(
  selectPlannerState,
  (state) => state.mapMode
);

export const selectPlannerResultMode = createSelector(
  selectPlannerState,
  (state) => state.resultMode
);

export const selectPlannerResultModeCompact = createSelector(
  selectPlannerResultMode,
  (resultMode) => resultMode === 'compact'
);

export const selectPlannerResultModeDetailed = createSelector(
  selectPlannerResultMode,
  (resultMode) => resultMode === 'detailed'
);

export const selectPlannerResultModeInstructions = createSelector(
  selectPlannerResultMode,
  (resultMode) => resultMode === 'instructions'
);

export const selectPlannerPosition = createSelector(
  selectPlannerState,
  (state) => state.position
);

export const selectPlannerLayerStates = createSelector(
  selectPlannerState,
  (state) => state.layerStates
);

export const selectPlannerPois = createSelector(
  selectPlannerState,
  (state) => state.pois
);

export const selectPlannerPoiLayerStates = createSelector(
  selectPlannerState,
  (state) => state.poiLayerStates
);

export const selectPlannerPoisEnabled = createSelector(
  selectPlannerState,
  (state) => state.pois === true
);

export const selectPlannerPoiGroupVisible = (layerName: string) =>
  createSelector(selectPlannerState, (state) => {
    const layerStates = state.poiLayerStates.filter(
      (layerState) => layerState.layerName === layerName
    );
    return layerStates.length === 1 && layerStates[0].visible;
  });
