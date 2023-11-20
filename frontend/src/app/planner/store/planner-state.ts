import { NetworkType } from '@api/custom';
import { MapLayerState } from '@app/ol/domain';
import { MapPosition } from '@app/ol/domain';
import { MapMode } from '@app/ol/services';

export const initialState: PlannerState = {
  networkType: NetworkType.hiking,
  position: null,
  mapMode: null,
  resultMode: null,
  layerStates: [],
  poiLayerStates: [],
};

export interface PlannerState {
  networkType: NetworkType;
  position: MapPosition;
  mapMode: MapMode;
  resultMode: string; // 'compact' | 'detailed'
  layerStates: MapLayerState[];
  poiLayerStates: MapLayerState[];
}

export const plannerFeatureKey = 'planner';
