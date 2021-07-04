import { Color } from 'ol/color';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import Text from 'ol/style/Text';
import { MainStyleColors } from '@app/components/ol/style/main-style-colors';

export const nameStyle = (): Style =>
  new Style({
    text: new Text({
      text: '',
      textAlign: 'center',
      textBaseline: 'middle',
      offsetY: 0,
      font: '14px Arial, Verdana, Helvetica, sans-serif',
      fill: new Fill({
        color: 'blue',
      }),
      stroke: new Stroke({
        color: MainStyleColors.white,
        width: 4,
      }),
    }),
  });

export const small = (color: Color): Style => {
  return new Style({
    image: new Circle({
      radius: 3,
      fill: new Fill({
        color: MainStyleColors.white,
      }),
      stroke: new Stroke({
        color,
        width: 2,
      }),
    }),
  });
};

export const large = (color: Color): Style => {
  return buildLarge(color, false);
};

export const proposedLarge = (color: Color): Style => {
  return buildLarge(color, true);
};

const buildLarge = (color: Color, proposed: boolean): Style => {
  const backgroundColor = proposed
    ? MainStyleColors.proposedWhite
    : MainStyleColors.white;
  const lineDash = proposed ? [3, 6] : null;

  return new Style({
    image: new Circle({
      radius: 14,
      fill: new Fill({
        color: backgroundColor,
      }),
      stroke: new Stroke({
        color,
        lineDash,
        width: 3,
      }),
    }),
    text: new Text({
      text: '',
      textAlign: 'center',
      textBaseline: 'middle',
      font: '14px Arial, Verdana, Helvetica, sans-serif',
      stroke: new Stroke({
        color: backgroundColor,
        width: 5,
      }),
    }),
  });
};
