import { NetworkType } from '@api/custom/network-type';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionPlannerNetworkType } from './planner-actions';
import { actionPlannerPoiGroupVisible } from './planner-actions';
import { actionPlannerPoisEnabled } from './planner-actions';
import { actionPlannerLayerVisible } from './planner-actions';
import { actionPlannerPosition } from './planner-actions';
import { actionPlannerResultMode } from './planner-actions';
import { actionPlannerMapMode } from './planner-actions';
import { actionPlannerLoad } from './planner-actions';
import { initialState } from './planner-state';
import { PlannerState } from './planner-state';

export const plannerReducer = createReducer<PlannerState>(
  initialState,
  on(actionPlannerLoad, (oldState, state): PlannerState => {
    const layerStates = determineLayerStates(state.networkType);
    return {
      ...state,
      layerStates,
    };
  }),
  on(actionPlannerNetworkType, (state, { networkType }): PlannerState => {
    const layerStates = determineLayerStates(networkType);
    return {
      ...state,
      networkType,
      layerStates,
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
    (state, position): PlannerState => ({
      ...state,
      position,
    })
  ),
  on(
    actionPlannerLayerVisible,
    (state, { layerName, visible }): PlannerState => {
      const layerStates = state.layerStates.map((layerState) => {
        if (layerState.layerName === layerName) {
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
    }
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

function determineLayerStates(networkType: NetworkType): MapLayerState[] {
  let layerStates: MapLayerState[] = [
    { layerName: 'osm', visible: true }, // TODO planner this should not be the default anymore
    { layerName: 'background', visible: false }, // TODO planner make this the default
    { layerName: networkType, visible: true },
  ];
  if (networkType === NetworkType.hiking) {
    layerStates.push({
      layerName: 'netherlands-hiking',
      visible: false,
    });
    layerStates.push({
      layerName: 'flanders-hiking',
      visible: false,
    });
  }

  const statesString = layerStates
    .filter((layerState) => layerState.visible)
    .map((layerState) => layerState.layerName)
    .join(',');
  console.log(`determineLayerStates: visible=${statesString}`);

  return layerStates;
}
