import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class ExploreStyleSurvey {
  private static readonly surveyStyle = new Style({
    zIndex: 11,
    stroke: new Stroke({
      color: '#ffff00',
      width: 2,
    }),
  });

  static style(zoom: number, survey: string): Style {
    return this.surveyStyle;
  }
}
