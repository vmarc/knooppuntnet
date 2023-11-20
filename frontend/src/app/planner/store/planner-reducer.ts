import { PoiTileLayerService } from '@app/ol/services';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionPlannerNetworkType } from './planner-actions';
import { actionPlannerPoiGroupVisible } from './planner-actions';
import { actionPlannerPoisVisible } from './planner-actions';
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
  on(actionPlannerLoad, (_, { state }): PlannerState => state),
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
  on(actionPlannerPoisVisible, (state, { visible }): PlannerState => {
    const layerStates = state.layerStates.map((layerState) => {
      if (layerState.layerName === PoiTileLayerService.poiLayerName) {
        return {
          ...layerState,
          visible,
        };
      }
      return layerState;
    });
    return {
      ...state,
      layerStates,
    };
  }),
  on(
    actionPlannerLayerStates,
    (state, { layerStates }): PlannerState => ({
      ...state,
      layerStates,
    })
  ),
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
