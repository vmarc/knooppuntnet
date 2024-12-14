import { SurveyDateValues } from '@app/core';
import { MainMapNodeStyle } from '@app/ol/style';
import { MainMapStyleParameters } from '@app/ol/style';
import { FeatureLike } from 'ol/Feature';
import Style from 'ol/style/Style';
import { MapStyleOptions } from '../../state/map-style-options';
import { ExploreStyleAnalysis } from './explore-style-analysis';
import { ExploreStyleFocus } from './explore-style-focus';
import { ExploreStyleStandard } from './explore-style-standard';
import { ExploreStyleSurvey } from './explore-style-survey';

export class ExploreStyle {
  static style(styleOptions: MapStyleOptions, feature: FeatureLike): Style | Array<Style> {
    const scope = feature.get('scope');
    if (!this.showScope(styleOptions, scope)) {
      return undefined;
    }

    const layer = feature.get('layer');
    if (layer === 'node') {
      return this.nodeStyle(styleOptions, feature);
    } else if (layer === 'route' || layer === 'node-route') {
      return this.routeStyle(styleOptions, feature);
    }
    return undefined;
  }

  private static nodeStyle(styleOptions: MapStyleOptions, feature: FeatureLike): Array<Style> {
    const baseStyle = this.baseNodeStyle(styleOptions, feature);
    if (baseStyle) {
      if (styleOptions.focusElements) {
        const nodeId = feature.get('id');
        if (styleOptions.focusElements.nodeIds.includes(nodeId)) {
          const focusStyle = ExploreStyleFocus.nodeStyle(styleOptions.zoom);
          return [...baseStyle, focusStyle];
        }
      }
    }
    return baseStyle;
  }

  private static baseNodeStyle(styleOptions: MapStyleOptions, feature: FeatureLike): Array<Style> {
    if (styleOptions.mode === 'survey') {
      if (styleOptions.surveyDateValues) {
        const survey = feature.get('survey');
        const proposed = feature.get('proposed') === 'true';
        const ref = feature.get('ref');
        const name = feature.get('name');

        let title: string;
        if (ref && ref !== 'o') {
          title = ref;
        } else {
          title = name;
        }

        return [
          ExploreStyleSurvey.nodeStyle(
            styleOptions.zoom,
            styleOptions.surveyDateValues,
            survey,
            proposed,
            title
          ),
        ];
      } else {
        return undefined;
      }
    }

    const parameters: MainMapStyleParameters = {
      mapMode: 'analysis',
      showProposed: true,
      surveyDateValues: new SurveyDateValues('', '', '', ''),
      selectedRouteId: null,
      selectedNodeId: null,
    };
    return new MainMapNodeStyle().nodeStyle(parameters, 0, feature);
  }

  private static routeStyle(
    styleOptions: MapStyleOptions,
    feature: FeatureLike
  ): Style | Array<Style> {
    const baseStyle = this.baseRouteStyle(styleOptions, feature);
    if (baseStyle && styleOptions.focusElements) {
      const routeId = feature.get('routeId');
      if (styleOptions.focusElements.routeIds.includes(routeId)) {
        const focusStyle = ExploreStyleFocus.routeStyle(styleOptions.zoom);
        return [baseStyle, focusStyle];
      }
    }
    return baseStyle;
  }

  private static baseRouteStyle(styleOptions: MapStyleOptions, feature: FeatureLike): Style {
    const scope = feature.get('scope');

    if (styleOptions.mode === 'survey') {
      const survey = feature.get('survey');
      return ExploreStyleSurvey.routeStyle(
        styleOptions.zoom,
        styleOptions.surveyDateValues,
        survey
      );
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
