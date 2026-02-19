import { ClientPoiConfiguration } from '@api/common/tiles/client-poi-configuration';
import { ClientPoiDefinition } from '@api/common/tiles/client-poi-definition';
import { ClientPoiGroupDefinition } from '@api/common/tiles/client-poi-group-definition';

export class InterpretedPoiConfiguration {
  private readonly poiDefinitionMap: Map<string, ClientPoiDefinition>;

  constructor(private configuration: ClientPoiConfiguration) {
    const keysAndValues: Array<[string, ClientPoiDefinition]> = [];
    configuration.groupDefinitions.forEach((g) => {
      g.poiDefinitions.forEach((d) => {
        keysAndValues.push([d.name, d]);
      });
    });
    this.poiDefinitionMap = new Map<string, ClientPoiDefinition>(keysAndValues);
  }

  poiDefinitionWithName(name: string): ClientPoiDefinition {
    return this.poiDefinitionMap.get(name);
  }

  getPoiDefinitionMap(): Map<string, ClientPoiDefinition> {
    return this.poiDefinitionMap;
  }

  getGroupDefinitions(): ReadonlyArray<ClientPoiGroupDefinition> {
    return this.configuration.groupDefinitions;
  }
}
