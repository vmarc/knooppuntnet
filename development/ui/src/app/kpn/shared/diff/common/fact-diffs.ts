// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../../fact';

export class FactDiffs {

  constructor(readonly resolved: List<Fact>,
              readonly introduced: List<Fact>,
              readonly remaining: List<Fact>) {
  }

  public static fromJSON(jsonObject): FactDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new FactDiffs(
      jsonObject.resolved ? List(jsonObject.resolved.map(json => Fact.fromJSON(json))) : List(),
      jsonObject.introduced ? List(jsonObject.introduced.map(json => Fact.fromJSON(json))) : List(),
      jsonObject.remaining ? List(jsonObject.remaining.map(json => Fact.fromJSON(json))) : List()
    );
  }
}
