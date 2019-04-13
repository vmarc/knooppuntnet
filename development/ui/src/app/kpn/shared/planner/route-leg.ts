// this class is generated, please do not modify

import {List} from "immutable";
import {RouteLegFragment} from "./route-leg-fragment";

export class RouteLeg {

  constructor(readonly legId: string,
              readonly fragments: List<RouteLegFragment>) {
  }

  public static fromJSON(jsonObject): RouteLeg {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLeg(
      jsonObject.legId,
      jsonObject.fragments ? List(jsonObject.fragments.map(json => RouteLegFragment.fromJSON(json))) : List()
    );
  }
}
