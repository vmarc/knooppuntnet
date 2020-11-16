import {Map} from 'immutable';
import Icon from 'ol/style/Icon';
import IconAnchorUnits from 'ol/style/IconAnchorUnits';
import Style from 'ol/style/Style';
import {InterpretedPoiConfiguration} from '../domain/interpreted-poi-configuration';

export class PoiStyleMap {

  private readonly poiStyleMap: Map<string, Style>;

  constructor(configuration: InterpretedPoiConfiguration) {
    this.poiStyleMap = configuration.getPoiDefinitionMap().map(poiDefinition => {
      const image = 'assets/images/pois/' + poiDefinition.icon;
      return new Style({
        image: new Icon({
          anchor: [0.5, 37],
          anchorXUnits: IconAnchorUnits.FRACTION,
          anchorYUnits: IconAnchorUnits.PIXELS,
          src: image
        })
      });
    });
  }

  get(poiName: string): Style {
    return this.poiStyleMap.get(poiName);
  }

}
