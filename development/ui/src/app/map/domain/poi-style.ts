import {Map} from 'immutable';
import {Icon, Style} from 'ol/style';
import {InterpretedPoiConfiguration} from "./interpreted-poi-configuration";

export class PoiStyle {

  private readonly poiIconStyleMap: Map<string, Style>;

  constructor(configuration: InterpretedPoiConfiguration) {
    this.poiIconStyleMap = configuration.getDefinitions().map(poiDefinition => {
      const image = "assets/images/pois/" + poiDefinition.icon;
      return new Style({
        image: new Icon({
          anchor: [0.5, 37],
          anchorXUnits: 'fraction',
          anchorYUnits: 'pixels',
          src: image
        })
      });
    });
  }

  get(layer: string): Style {
    return this.poiIconStyleMap.get(layer);
  }

}
