// this class is generated, please do not modify

import {List} from "immutable";
import {NameValue} from "./name-value";

export class BarChart {

  constructor(readonly data: List<NameValue>) {
  }

  public static fromJSON(jsonObject: any): BarChart {
    if (!jsonObject) {
      return undefined;
    }
    return new BarChart(
      jsonObject.data ? List(jsonObject.data.map((json: any) => NameValue.fromJSON(json))) : List()
    );
  }
}
