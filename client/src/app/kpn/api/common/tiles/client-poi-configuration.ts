// this class is generated, please do not modify

import { ClientPoiGroupDefinition } from './client-poi-group-definition';

export class ClientPoiConfiguration {
  constructor(readonly groupDefinitions: Array<ClientPoiGroupDefinition>) {}

  static fromJSON(jsonObject: any): ClientPoiConfiguration {
    if (!jsonObject) {
      return undefined;
    }
    return new ClientPoiConfiguration(
      jsonObject.groupDefinitions.map((json: any) =>
        ClientPoiGroupDefinition.fromJSON(json)
      )
    );
  }
}
