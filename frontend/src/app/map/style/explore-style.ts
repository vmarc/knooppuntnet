import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

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

  private static readonly colorInternational = '#ff0000';
  private static readonly colorNational = '#0000ff';
  private static readonly colorRegional = '#00ff00';
  private static readonly colorLocal = '#ff8800';

  private static readonly zIndexInternational = 11;
  private static readonly zIndexNational = 12;
  private static readonly zIndexRegional = 13;
  private static readonly zIndexLocal = 14;

  private static readonly width1 = 2;
  private static readonly width2 = 4;
  private static readonly width3 = 6;
  private static readonly width4 = 8;

  private static readonly internationalRouteStyle1 = this.buildInternationalRouteStyle(this.width1);
  private static readonly internationalRouteStyle3 = this.buildInternationalRouteStyle(this.width3);
  private static readonly internationalRouteStyle4 = this.buildInternationalRouteStyle(this.width4);
  private static readonly nationalRouteStyle2 = this.buildNationalRouteStyle(this.width2);
  private static readonly nationalRouteStyle3 = this.buildNationalRouteStyle(this.width3);
  private static readonly regionalRouteStyle1 = this.buildRegionalRouteStyle(this.width1);
  private static readonly regionalRouteStyle2 = this.buildRegionalRouteStyle(this.width2);
  private static readonly localRouteStyle1 = this.buildLocalRouteStyle(this.width1);

  private static buildInternationalRouteStyle(width: number): Style {
    return new Style({
      zIndex: this.zIndexInternational,
      stroke: new Stroke({
        color: this.colorInternational,
        width: width,
      }),
    });
  }

  private static buildNationalRouteStyle(width: number): Style {
    return new Style({
      zIndex: this.zIndexNational,
      stroke: new Stroke({
        color: this.colorNational,
        width: width,
      }),
    });
  }

  private static buildRegionalRouteStyle(width: number): Style {
    return new Style({
      zIndex: this.zIndexRegional,
      stroke: new Stroke({
        color: this.colorRegional,
        width: width,
      }),
    });
  }

  private static buildLocalRouteStyle(width: number): Style {
    return new Style({
      zIndex: this.zIndexLocal,
      stroke: new Stroke({
        color: this.colorLocal,
        width: width,
      }),
    });
  }

  static style(scope: string, zoom: number): Style {
    let style: Style = null;
    if (scope == 'international') {
      if (zoom == 6 || zoom == 7) {
        style = this.internationalRouteStyle1;
      } else if (zoom == 8 || zoom == 9) {
        style = this.internationalRouteStyle3;
      } else {
        style = this.internationalRouteStyle4;
      }
    } else if (scope == 'national') {
      if (zoom == 8 || zoom == 9) {
        style = this.nationalRouteStyle2;
      } else if (zoom == 8 || zoom == 9) {
        style = this.nationalRouteStyle3;
      } else {
        style = this.nationalRouteStyle3;
      }
    } else if (scope == 'regional') {
      if (zoom == 8 || zoom == 9) {
        style = this.regionalRouteStyle1;
      } else if (zoom == 10 || zoom == 11) {
        style = this.regionalRouteStyle2;
      } else {
        style = this.regionalRouteStyle2;
      }
    } else {
      style = this.localRouteStyle1;
    }

    // if (this.selectedRoute()) {
    //   if (routeId && routeId == this.selectedRoute()) {
    //     style = this.selectedRouteStyle;
    //   } else {
    //     style = this.unselectedRouteStyle;
    //   }
    // } else {
    //   style = this.selectedRouteStyle;
    // }

    return style;
  }
}
