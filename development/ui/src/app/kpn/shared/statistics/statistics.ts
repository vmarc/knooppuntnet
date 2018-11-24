import {Map} from 'immutable';
import {Statistic} from './statistic';

export class Statistics {
  readonly map: Map<string, Statistic>;

  constructor(map: Map<string, Statistic>) {
    this.map = map;
  }

  public static fromJSON(jsonObject): Statistics {
    if (!jsonObject) {
      return undefined;
    }
    return undefined;
    // return new Statistics(
    //   Map<string, Statistic>.fromJSON(jsonObject.map)
    // );
  }
}
