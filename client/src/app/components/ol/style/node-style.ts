import {Color} from 'ol/color';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import Text from 'ol/style/Text';
import {MainStyleColors} from './main-style-colors';

export class NodeStyle {

  public static readonly smallLightGreen = NodeStyle.small(MainStyleColors.lightGreen);
  public static readonly smallGreen = NodeStyle.small(MainStyleColors.green);
  public static readonly smallDarkGreen = NodeStyle.small(MainStyleColors.darkGreen);
  public static readonly smallVeryDarkGreen = NodeStyle.small(MainStyleColors.veryDarkGreen);
  public static readonly smallRed = NodeStyle.small(MainStyleColors.red);
  public static readonly smallDarkRed = NodeStyle.small(MainStyleColors.darkRed);
  public static readonly smallGray = NodeStyle.small(MainStyleColors.gray);
  public static readonly smallBlue = NodeStyle.small(MainStyleColors.blue);
  public static readonly smallDarkBlue = NodeStyle.small(MainStyleColors.darkBlue);

  public static readonly largeGreen = NodeStyle.buildLarge(MainStyleColors.green, false);
  public static readonly largeGray = NodeStyle.buildLarge(MainStyleColors.gray, false);
  public static readonly largeLightGreen = NodeStyle.buildLarge(MainStyleColors.lightGreen, false);
  public static readonly largeDarkGreen = NodeStyle.buildLarge(MainStyleColors.darkGreen, false);
  public static readonly largeVeryDarkGreen = NodeStyle.buildLarge(MainStyleColors.veryDarkGreen, false);
  public static readonly largeDarkRed = NodeStyle.buildLarge(MainStyleColors.darkRed, false);
  public static readonly largeBlue = NodeStyle.buildLarge(MainStyleColors.blue, false);
  public static readonly largeDarkBlue = NodeStyle.buildLarge(MainStyleColors.darkBlue, false);

  public static readonly proposedLargeGreen = NodeStyle.buildLarge(MainStyleColors.green, true);
  public static readonly proposedLargeGray = NodeStyle.buildLarge(MainStyleColors.gray, true);
  public static readonly proposedLargeLightGreen = NodeStyle.buildLarge(MainStyleColors.lightGreen, true);
  public static readonly proposedLargeDarkGreen = NodeStyle.buildLarge(MainStyleColors.darkGreen, true);
  public static readonly proposedLargeVeryDarkGreen = NodeStyle.buildLarge(MainStyleColors.veryDarkGreen, true);
  public static readonly proposedLargeDarkRed = NodeStyle.buildLarge(MainStyleColors.darkRed, true);
  public static readonly proposedLargeBlue = NodeStyle.buildLarge(MainStyleColors.blue, true);
  public static readonly proposedLargeDarkBlue = NodeStyle.buildLarge(MainStyleColors.darkBlue, true);

  public static nameStyle(): Style {
    return new Style({
      text: new Text({
        text: '',
        textAlign: 'center',
        textBaseline: 'middle',
        offsetY: 0,
        font: '14px Arial, Verdana, Helvetica, sans-serif',
        fill: new Fill({
          color: 'blue'
        }),
        stroke: new Stroke({
          color: MainStyleColors.white,
          width: 4
        })
      })
    });
  }

  private static small(color: Color): Style {
    return new Style({
      image: new Circle({
        radius: 3,
        fill: new Fill({
          color: MainStyleColors.white
        }),
        stroke: new Stroke({
          color,
          width: 2
        })
      })
    });
  }

  private static buildLarge(color: Color, proposed: boolean): Style {

    const backgroundColor = proposed ? [240, 240, 240] : MainStyleColors.white;
    const lineDash = proposed ? [3, 6] : null;

    return new Style({
      image: new Circle({
        radius: 14,
        fill: new Fill({
          color: backgroundColor
        }),
        stroke: new Stroke({
          color: color,
          lineDash: lineDash,
          width: 3
        })
      }),
      text: new Text({
        text: '',
        textAlign: 'center',
        textBaseline: 'middle',
        font: '14px Arial, Verdana, Helvetica, sans-serif',
        stroke: new Stroke({
          color: backgroundColor,
          width: 5
        })
      })
    });
  }
}
