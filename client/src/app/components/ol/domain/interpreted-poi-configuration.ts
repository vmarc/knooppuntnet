import { ClientPoiConfiguration } from '@api/common/tiles/client-poi-configuration';
import { ClientPoiDefinition } from '@api/common/tiles/client-poi-definition';
import { ClientPoiGroupDefinition } from '@api/common/tiles/client-poi-group-definition';
import { Map } from 'immutable';

export class InterpretedPoiConfiguration {
  private readonly poiDefinitionMap: Map<string, ClientPoiDefinition>;

  constructor(private configuration: ClientPoiConfiguration) {
    const keysAndValues: Array<[string, ClientPoiDefinition]> = [];
    configuration.groupDefinitions.forEach((g) => {
      g.poiDefinitions.forEach((d) => {
        keysAndValues.push([d.name, d]);
      });
    });
    this.poiDefinitionMap = Map(keysAndValues);
  }

  poiDefinitionWithName(name: string): ClientPoiDefinition {
    return this.poiDefinitionMap.get(name);
  }

  getPoiDefinitionMap(): Map<string, ClientPoiDefinition> {
    return this.poiDefinitionMap;
  }

  getGroupDefinitions(): ClientPoiGroupDefinition[] {
    return this.configuration.groupDefinitions;
  }
}
