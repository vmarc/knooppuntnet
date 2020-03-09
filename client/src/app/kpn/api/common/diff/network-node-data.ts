// this class is generated, please do not modify

import {Country} from "../../custom/country";
import {RawNode} from "../data/raw/raw-node";

export class NetworkNodeData {

  constructor(readonly node: RawNode,
              readonly name: string,
              readonly country: Country) {
  }

  public static fromJSON(jsonObject: any): NetworkNodeData {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodeData(
      RawNode.fromJSON(jsonObject.node),
      jsonObject.name,
      Country.fromJSON(jsonObject.country)
    );
  }
}
