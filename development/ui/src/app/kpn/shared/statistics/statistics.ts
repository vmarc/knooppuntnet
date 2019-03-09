import {Map} from 'immutable';
import {Statistic} from './statistic';

export class Statistics {
  readonly map: Map<string, Statistic>;

  constructor(map: Map<string, Statistic>) {
    this.map = map;
  }

  public get(key: string): Statistic {
    return this.map.get(key, null);
  }

  public static fromJSON(jsonObject): Statistics {
    if (!jsonObject) {
      return undefined;
    }
    const keysAndValues = [];
    Object.keys(jsonObject.map).forEach(key => {
      const value = jsonObject.map[key];
      const s = Statistic.fromJSON(value);
      keysAndValues.push([key, s]);
    });
    const map = Map<string, Statistic>(keysAndValues);
    return new Statistics(map);
  }
}
