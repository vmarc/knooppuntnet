// this class is generated, please do not modify

import {NameValue} from './name-value';

export class BarChart2dValue {

  constructor(readonly name: string,
              readonly series: Array<NameValue>) {
  }

  public static fromJSON(jsonObject: any): BarChart2dValue {
    if (!jsonObject) {
      return undefined;
    }
    return new BarChart2dValue(
      jsonObject.name,
      jsonObject.series?.map((json: any) => NameValue.fromJSON(json))
    );
  }
}
