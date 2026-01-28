import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class ExploreStyleAnalysis {
  private static readonly colorOk = '#00cc00';
  private static readonly colorNok = '#ff0000';

  private static readonly zIndexOk = 11;
  private static readonly zIndexNok = 12;

  private static readonly width1 = 1;
  private static readonly width2 = 2;
  private static readonly width3 = 4;

  private static readonly okStyle1 = this.buildOkStyle(this.width1);
  private static readonly okStyle2 = this.buildOkStyle(this.width2);
  private static readonly okStyle3 = this.buildOkStyle(this.width3);
  private static readonly nokStyle1 = this.buildNokStyle(this.width1);
  private static readonly nokStyle2 = this.buildNokStyle(this.width2);

  private static buildOkStyle(width: number): Style {
    return this.buildStyle(this.zIndexOk, this.colorOk, width);
  }

  private static buildNokStyle(width: number): Style {
    return this.buildStyle(this.zIndexNok, this.colorNok, width);
  }

  private static buildStyle(zIndex: number, color: string, width: number): Style {
    return new Style({
      zIndex: zIndex,
      stroke: new Stroke({
        color: color,
        width: width,
      }),
    });
  }

  static style(zoom: number, error: string): Style {
    let style: Style = undefined;
    if (error === 'true') {
      if (zoom < 10) {
        style = this.nokStyle1;
      } else if (zoom < 13) {
        style = this.nokStyle2;
      } else {
        style = this.nokStyle2;
      }
    } else {
      if (zoom < 10) {
        style = this.okStyle1;
      } else if (zoom < 13) {
        style = this.okStyle2;
      } else {
        style = this.okStyle3;
      }
    }
    return style;
  }
}
