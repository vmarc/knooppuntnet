// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../../fact';

export class FactDiffs {
  readonly resolved: List<Fact>;
  readonly introduced: List<Fact>;
  readonly remaining: List<Fact>;

  constructor(resolved: List<Fact>,
              introduced: List<Fact>,
              remaining: List<Fact>) {
    this.resolved = resolved;
    this.introduced = introduced;
    this.remaining = remaining;
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
