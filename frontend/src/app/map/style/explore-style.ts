import { SurveyDateValues } from '@app/core';
import { MainMapNodeStyle } from '@app/ol/style';
import { MainMapStyleParameters } from '@app/ol/style';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { MapStyleOptions } from '../../state/map-style-options';
import { ExploreStyleAnalysis } from './explore-style-analysis';
import { ExploreStyleStandard } from './explore-style-standard';
import { ExploreStyleSurvey } from './explore-style-survey';

export class ExploreStyle {
  private static unselectedRouteStyle = new Style({
    stroke: new Stroke({
      color: [130, 130, 130],
      width: 3,
    }),
  });

  private static readonly selectedRouteStyle = new Style({
    stroke: new Stroke({
      color: [0, 0, 255],
      width: 3,
    }),
  });

  static style(styleOptions: MapStyleOptions, feature: FeatureLike): Style | Array<Style> {
    const layer = feature.get('layer');
    console.log('layer', layer);

    if (layer === 'node') {
      const parameters: MainMapStyleParameters = {
        mapMode: 'analysis',
        showProposed: true,
        surveyDateValues: new SurveyDateValues('', '', '', ''),
        selectedRouteId: null,
        selectedNodeId: null,
      };
      return new MainMapNodeStyle().nodeStyle(parameters, 0, feature);
    }

    const scope = feature.get('scope');
    if (!this.showScope(styleOptions, scope)) {
      return undefined;
    }
    if (styleOptions.mode === 'survey') {
      const survey = feature.get('survey');
      return ExploreStyleSurvey.style(styleOptions.zoom, survey);
    }
    if (styleOptions.mode === 'analysis') {
      const error = feature.get('error');
      return ExploreStyleAnalysis.style(styleOptions.zoom, error);
    }
    return ExploreStyleStandard.style(styleOptions.zoom, scope);
  }

  private static showScope(styleOptions: MapStyleOptions, scope: string | undefined): boolean {
    if (styleOptions.scopeInternational && scope === 'international') {
      return true;
    }

    if (styleOptions.scopeNational && scope === 'national') {
      return true;
    }

    if (styleOptions.scopeRegional && scope === 'regional') {
      return true;
    }

    if (styleOptions.scopeLocal && scope === 'local') {
      return true;
    }

    return styleOptions.scopeNodeRoutes && scope === undefined;
  }
}
