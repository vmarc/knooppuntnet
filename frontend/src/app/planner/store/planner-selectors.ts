import { PoiTileLayerService } from '@app/ol/services';
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

export const selectPlannerLayerStates = createSelector(
  selectPlannerState,
  (state) => state.layerStates
);

export const selectPlannerPoisVisible = createSelector(
  selectPlannerState,
  (state) => {
    let visible = false;
    const poiLayerState = state.layerStates.find(
      (ls) => ls.layerName == PoiTileLayerService.poiLayerName
    );
    if (poiLayerState) {
      visible = poiLayerState.visible;
    }
    return visible;
  }
);

export const selectPlannerPoiLayerStates = createSelector(
  selectPlannerState,
  (state) => state.poiLayerStates
);

export const selectPlannerPoiGroupVisible = (layerName: string) =>
  createSelector(selectPlannerState, (state) => {
    const layerStates = state.poiLayerStates.filter(
      (layerState) => layerState.layerName === layerName
    );
    return layerStates.length === 1 && layerStates[0].visible;
  });
