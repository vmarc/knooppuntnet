import {Map} from "immutable";
import {Statistic} from "../common/statistics/statistic";

export class Statistics {

  constructor(readonly map: Map<string, Statistic>) {
  }

  public static fromJSON(jsonObject: any): Statistics {
    if (!jsonObject) {
      return undefined;
    }
    const keysAndValues: Array<[string, Statistic]> = [];
    Object.keys(jsonObject.map).forEach(key => {
      const value = jsonObject.map[key];
      const s = Statistic.fromJSON(value);
      keysAndValues.push([key, s]);
    });
    const map = Map<string, Statistic>(keysAndValues);
    return new Statistics(map);
  }

  public get(key: string): Statistic {
    return this.map.get(key, null);
  }

}
