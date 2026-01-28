import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { ExploreStyleConstants } from './explore-style-constants';

export class ExploreStyleStandard {
  static readonly colorInternational = '#ff0000';
  static readonly colorNational = '#0000ff';
  static readonly colorRegional = '#00cc00';
  static readonly colorLocal = '#ff8800';

  private static readonly width1 = 1;
  private static readonly width2 = 2;
  private static readonly width3 = 4;
  private static readonly width4 = 6;
  private static readonly width5 = 8;

  private static readonly internationalRouteStyle1 = this.buildInternationalRouteStyle(this.width1);
  private static readonly internationalRouteStyle2 = this.buildInternationalRouteStyle(this.width2);
  private static readonly internationalRouteStyle3 = this.buildInternationalRouteStyle(this.width3);
  private static readonly internationalRouteStyle4 = this.buildInternationalRouteStyle(this.width4);
  private static readonly internationalRouteStyle5 = this.buildInternationalRouteStyle(this.width5);
  private static readonly nationalRouteStyle1 = this.buildNationalRouteStyle(this.width1);
  private static readonly nationalRouteStyle2 = this.buildNationalRouteStyle(this.width2);
  private static readonly nationalRouteStyle3 = this.buildNationalRouteStyle(this.width3);
  private static readonly nationalRouteStyle4 = this.buildNationalRouteStyle(this.width4);
  private static readonly regionalRouteStyle2 = this.buildRegionalRouteStyle(this.width2);
  private static readonly regionalRouteStyle3 = this.buildRegionalRouteStyle(this.width3);
  private static readonly localRouteStyle1 = this.buildLocalRouteStyle(this.width2);

  private static buildInternationalRouteStyle(width: number): Style {
    return new Style({
      zIndex: ExploreStyleConstants.zIndexInternational,
      stroke: new Stroke({
        color: this.colorInternational,
        width: width,
      }),
    });
  }

  private static buildNationalRouteStyle(width: number): Style {
    return new Style({
      zIndex: ExploreStyleConstants.zIndexNational,
      stroke: new Stroke({
        color: this.colorNational,
        width: width,
      }),
    });
  }

  private static buildRegionalRouteStyle(width: number): Style {
    return new Style({
      zIndex: ExploreStyleConstants.zIndexRegional,
      stroke: new Stroke({
        color: this.colorRegional,
        width: width,
      }),
    });
  }

  private static buildLocalRouteStyle(width: number): Style {
    return new Style({
      zIndex: ExploreStyleConstants.zIndexLocal,
      stroke: new Stroke({
        color: this.colorLocal,
        width: width,
      }),
    });
  }

  static style(zoom: number, scope: string): Style {
    let style: Style = null;
    if (scope == 'international') {
      if (zoom < 5) {
        style = this.internationalRouteStyle1;
      } else if (zoom < 9) {
        style = this.internationalRouteStyle2;
      } else if (zoom < 11) {
        style = this.internationalRouteStyle3;
      } else if (zoom < 10) {
        style = this.internationalRouteStyle4;
      } else {
        style = this.internationalRouteStyle5;
      }
    } else if (scope == 'national') {
      if (zoom < 9) {
        style = this.nationalRouteStyle1;
      } else if (zoom < 10) {
        style = this.nationalRouteStyle2;
      } else if (zoom < 11) {
        style = this.nationalRouteStyle3;
      } else {
        style = this.nationalRouteStyle4;
      }
    } else if (scope == 'regional') {
      if (zoom < 10) {
        style = this.regionalRouteStyle2;
      } else if (zoom < 11) {
        style = this.regionalRouteStyle2;
      } else {
        style = this.regionalRouteStyle3;
      }
    } else {
      style = this.localRouteStyle1;
    }
    return style;
  }
}
