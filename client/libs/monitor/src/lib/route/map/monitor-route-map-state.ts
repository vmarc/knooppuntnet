import { MonitorRouteSegment } from '@api/common/monitor';
import { MonitorRouteDeviation } from '@api/common/monitor';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MonitorMapMode } from './monitor-map-mode';

export const initialState: MonitorRouteMapState = {
  page: null,
  mode: MonitorMapMode.comparison,
  referenceVisible: false,
  matchesVisible: false,
  deviationsVisible: false,
  osmRelationVisible: false,
  osmRelationAvailable: false,
  osmRelationEmpty: false,
  selectedDeviation: null,
  selectedOsmSegment: null,
  referenceType: 'osm',
  referenceAvailable: false,
  matchesEnabled: false,
  gpxDeviationsEnabled: false,
  osmRelationEnabled: false,
  deviations: [],
  osmSegments: [],
};

export interface MonitorRouteMapState {
  readonly page: MonitorRouteMapPage | null;

  readonly mode: MonitorMapMode;
  readonly referenceVisible: boolean;
  readonly matchesVisible: boolean;
  readonly deviationsVisible: boolean;
  readonly osmRelationVisible: boolean;
  readonly osmRelationAvailable: boolean;
  readonly osmRelationEmpty: boolean;
  readonly selectedDeviation: MonitorRouteDeviation | null;
  readonly selectedOsmSegment: MonitorRouteSegment | null;

  readonly referenceType: string;
  readonly referenceAvailable: boolean;
  readonly matchesEnabled: boolean;
  readonly gpxDeviationsEnabled: boolean;
  readonly osmRelationEnabled: boolean;
  readonly deviations: MonitorRouteDeviation[];
  readonly osmSegments: MonitorRouteSegment[];
}
