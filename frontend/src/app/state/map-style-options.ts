import { MapMode } from '@app/map/domain/map-mode';
import { SurveyDateValues } from '@app/shared/core/shared/survey-date-values';
import { SegmentMap } from '@app/state/segment-map';
import { FocusElements } from './focus-elements';

export interface MapStyleOptions {
  zoom: number;
  mode: MapMode;
  scopeInternational: boolean;
  scopeNational: boolean;
  scopeRegional: boolean;
  scopeLocal: boolean;
  scopeNodeRoutes: boolean;
  selectedRoute: number | undefined;
  surveyDateValues: SurveyDateValues | undefined;
  focusElements: FocusElements;
  segmentMap: SegmentMap | undefined;
}
