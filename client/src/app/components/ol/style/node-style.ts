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

  public static readonly largeGreen = NodeStyle.large(MainStyleColors.green);
  public static readonly largeGray = NodeStyle.large(MainStyleColors.gray);

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

  private static large(color: Color): Style {
    return new Style({
      image: new Circle({
        radius: 14,
        fill: new Fill({
          color: MainStyleColors.white
        }),
        stroke: new Stroke({
          color,
          width: 3
        })
      }),
      text: new Text({
        text: '',
        textAlign: 'center',
        textBaseline: 'middle',
        font: '14px Arial, Verdana, Helvetica, sans-serif',
        stroke: new Stroke({
          color: MainStyleColors.white,
          width: 5
        })
      })
    });
  }
}
