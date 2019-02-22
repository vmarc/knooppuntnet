// this class is generated, please do not modify

import {List} from 'immutable';
import {ClientPoiDefinition} from './client-poi-definition';

export class ClientPoiGroupDefinition {

  constructor(readonly name: string,
              readonly enabledDefault: boolean,
              readonly poiDefinitions: List<ClientPoiDefinition>) {
  }

  public static fromJSON(jsonObject): ClientPoiGroupDefinition {
    if (!jsonObject) {
      return undefined;
    }
    return new ClientPoiGroupDefinition(
      jsonObject.name,
      jsonObject.enabledDefault,
      jsonObject.poiDefinitions ? List(jsonObject.poiDefinitions.map(json => ClientPoiDefinition.fromJSON(json))) : List()
    );
  }
}
