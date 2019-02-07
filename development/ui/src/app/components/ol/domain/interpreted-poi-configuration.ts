import {Map} from 'immutable';

import {TilePoiConfiguration} from "../../../kpn/shared/tiles/tile-poi-configuration";
import {TilePoiDefinition} from "../../../kpn/shared/tiles/tile-poi-definition";

export class InterpretedPoiConfiguration {

  private readonly definitions: Map<string, TilePoiDefinition>;

  constructor(private configuration: TilePoiConfiguration) {
    this.definitions = Map(
      configuration.groups.flatMap(g => g.definitions).map(d => {
        const keyAndValue: [string, TilePoiDefinition] = [d.name, d];
        return keyAndValue;
      })
    );
  }

  definitionWithName(name: string): TilePoiDefinition {
    return this.definitions.get(name);
  }

  getDefinitions(): Map<string, TilePoiDefinition> {
    return this.definitions;
  }

  getAllDefinitions(): IterableIterator<TilePoiDefinition> {
    return this.definitions.values();
  }

}
