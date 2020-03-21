// this class is generated, please do not modify

import {List} from "immutable";
import {NameValue} from "./name-value";

export class BarChart2dValue {

  constructor(readonly name: string,
              readonly series: List<NameValue>) {
  }

  public static fromJSON(jsonObject: any): BarChart2dValue {
    if (!jsonObject) {
      return undefined;
    }
    return new BarChart2dValue(
      jsonObject.name,
      jsonObject.series ? List(jsonObject.series.map((json: any) => NameValue.fromJSON(json))) : List()
    );
  }
}
