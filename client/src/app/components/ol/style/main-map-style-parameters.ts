import { MapMode } from '@app/components/ol/services/map-mode';
import { SurveyDateValues } from '@app/components/ol/services/survey-date-values';

export class MainMapStyleParameters {
  constructor(
    public mapMode: MapMode,
    public showProposed: boolean,
    public surveyDateValues: SurveyDateValues,
    public selectedRouteId: string,
    public selectedNodeId: string,
    public highlightedRouteId: string
  ) {}
}
