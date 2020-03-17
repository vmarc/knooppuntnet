// this class is generated, please do not modify

import {List} from "immutable";
import {Ref} from "../common/ref";
import {Timestamp} from "../../custom/timestamp";

export class MapNodeDetail {

  constructor(readonly id: number,
              readonly name: string,
              readonly lastUpdated: Timestamp,
              readonly networkReferences: List<Ref>,
              readonly routeReferences: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): MapNodeDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new MapNodeDetail(
      jsonObject.id,
      jsonObject.name,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      jsonObject.networkReferences ? List(jsonObject.networkReferences.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.routeReferences ? List(jsonObject.routeReferences.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
