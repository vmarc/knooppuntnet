import { SurveyDateValues } from '@app/shared/core/shared/survey-date-values';
import { MapMode } from '@app/ol/services/map-mode';

export class MainMapStyleParameters {
  constructor(
    public mapMode: MapMode,
    public showProposed: boolean,
    public surveyDateValues: SurveyDateValues,
    public selectedRouteId: string,
    public selectedNodeId: string
  ) {}
}
