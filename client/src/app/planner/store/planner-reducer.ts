import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionPlannerNetworkType } from './planner-actions';
import { actionPlannerPoiGroupVisible } from './planner-actions';
import { actionPlannerPoisEnabled } from './planner-actions';
import { actionPlannerPosition } from './planner-actions';
import { actionPlannerResultMode } from './planner-actions';
import { actionPlannerMapMode } from './planner-actions';
import { actionPlannerLoad } from './planner-actions';
import { actionPlannerLayerStates } from './planner-actions';
import { actionPlannerMapFinalized } from './planner-actions';
import { initialState } from './planner-state';
import { PlannerState } from './planner-state';

export const plannerReducer = createReducer<PlannerState>(
  initialState,
  on(
    actionPlannerLoad,
    (state, { networkType, mapMode, resultMode }): PlannerState => {
      return {
        ...initialState,
        networkType,
        mapMode,
        resultMode,
      };
    }
  ),
  on(
    actionPlannerMapFinalized,
    (state, { position, layerStates }): PlannerState => {
      return {
        ...state,
        position,
        layerStates,
      };
    }
  ),
  on(actionPlannerNetworkType, (state, { networkType }): PlannerState => {
    return {
      ...state,
      networkType,
    };
  }),
  on(
    actionPlannerMapMode,
    (state, { mapMode }): PlannerState => ({
      ...state,
      mapMode,
    })
  ),
  on(
    actionPlannerResultMode,
    (state, { resultMode }): PlannerState => ({
      ...state,
      resultMode,
    })
  ),
  on(
    actionPlannerPosition,
    (state, { mapPosition }): PlannerState => ({
      ...state,
      position: mapPosition,
    })
  ),
  on(
    actionPlannerLayerStates,
    (state, { layerStates }): PlannerState => ({
      ...state,
      layerStates,
    })
  ),
  on(actionPlannerPoisEnabled, (state, { enabled }): PlannerState => {
    const pois = enabled;
    return {
      ...state,
      pois,
    };
  }),
  on(
    actionPlannerPoiGroupVisible,
    (state, { groupName, visible }): PlannerState => {
      const poiLayerStates = state.poiLayerStates.map((layerState) => {
        if (layerState.layerName === groupName) {
          return {
            ...layerState,
            visible,
          };
        }
        return layerState;
      });
      return {
        ...state,
        poiLayerStates,
      };
    }
  )
);
