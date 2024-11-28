import Icon from 'ol/style/Icon';
import Style from 'ol/style/Style';
import { InterpretedPoiConfiguration } from '../../state/poi/interpreted-poi-configuration';

export class PoiStyleMap {
  private readonly poiStyleMap: ReadonlyMap<string, Style>;

  constructor(configuration: InterpretedPoiConfiguration) {
    const xmap = configuration.getPoiDefinitionMap();

    const entries: [string, Style][] = Array.from(xmap.entries()).map(([name, poiDefinition]) => {
      const image = 'assets/images/pois/' + poiDefinition.icon;
      const style = new Style({
        image: new Icon({
          anchor: [0.5, 37],
          anchorXUnits: 'fraction',
          anchorYUnits: 'pixels',
          src: image,
        }),
      });
      return [name, style];
    });

    this.poiStyleMap = new Map(entries);
  }

  get(poiName: string): Style {
    return this.poiStyleMap.get(poiName);
  }
}
