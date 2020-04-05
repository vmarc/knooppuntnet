// this class is generated, please do not modify

import {List} from "immutable";

export class LocationNode {

  constructor(readonly name: string,
              readonly nodeCount: number,
              readonly children: List<LocationNode>) {
  }

  public static fromJSON(jsonObject: any): LocationNode {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationNode(
      jsonObject.name,
      jsonObject.nodeCount,
      jsonObject.children ? List(jsonObject.children.map((json: any) => LocationNode.fromJSON(json))) : List()
    );
  }
}
