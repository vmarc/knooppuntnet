import {List, Map} from 'immutable';

import {ClientPoiConfiguration} from "../../../kpn/shared/tiles/client-poi-configuration";
import {ClientPoiDefinition} from "../../../kpn/shared/tiles/client-poi-definition";
import {ClientPoiGroupDefinition} from "../../../kpn/shared/tiles/client-poi-group-definition";

export class InterpretedPoiConfiguration {

  private readonly poiDefinitionMap: Map<string, ClientPoiDefinition>;

  constructor(private configuration: ClientPoiConfiguration) {
    this.poiDefinitionMap = Map(
      configuration.groupDefinitions.flatMap(g => g.poiDefinitions).map(d => {
        const keyAndValue: [string, ClientPoiDefinition] = [d.name, d];
        return keyAndValue;
      })
    );
  }

  poiDefinitionWithName(name: string): ClientPoiDefinition {
    return this.poiDefinitionMap.get(name);
  }

  getPoiDefinitionMap(): Map<string, ClientPoiDefinition> {
    return this.poiDefinitionMap;
  }

  getGroupDefinitions(): List<ClientPoiGroupDefinition> {
    return this.configuration.groupDefinitions;
  }
}
