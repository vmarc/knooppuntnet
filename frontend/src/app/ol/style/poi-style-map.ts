import { Map } from 'immutable';
import Icon from 'ol/style/Icon';
import Style from 'ol/style/Style';
import { InterpretedPoiConfiguration } from '../domain';

export class PoiStyleMap {
  private readonly poiStyleMap: Map<string, Style>;

  constructor(configuration: InterpretedPoiConfiguration) {
    this.poiStyleMap = configuration
      .getPoiDefinitionMap()
      .map((poiDefinition) => {
        const image = 'assets/images/pois/' + poiDefinition.icon;
        return new Style({
          image: new Icon({
            anchor: [0.5, 37],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            src: image,
          }),
        });
      });
  }

  get(poiName: string): Style {
    return this.poiStyleMap.get(poiName);
  }
}
