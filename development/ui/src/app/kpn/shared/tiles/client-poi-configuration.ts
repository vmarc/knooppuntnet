// this class is generated, please do not modify

import {List} from "immutable";
import {ClientPoiGroupDefinition} from "./client-poi-group-definition";

export class ClientPoiConfiguration {

  constructor(readonly groupDefinitions: List<ClientPoiGroupDefinition>) {
  }

  public static fromJSON(jsonObject): ClientPoiConfiguration {
    if (!jsonObject) {
      return undefined;
    }
    return new ClientPoiConfiguration(
      jsonObject.groupDefinitions ? List(jsonObject.groupDefinitions.map(json => ClientPoiGroupDefinition.fromJSON(json))) : List()
    );
  }
}
