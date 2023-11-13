// this class is generated, please do not modify

import { List } from 'immutable';
import { RouteLegFragment } from './route-leg-fragment';

export class RouteLegSegment {
  constructor(
    readonly meters: number,
    readonly surface: string,
    readonly colour: string,
    readonly fragments: List<RouteLegFragment>
  ) {}

  static fromJSON(jsonObject: any): RouteLegSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLegSegment(
      jsonObject.meters,
      jsonObject.surface,
      jsonObject.colour,
      jsonObject.fragments
        ? List(
            jsonObject.fragments.map((json: any) =>
              RouteLegFragment.fromJSON(json)
            )
          )
        : List()
    );
  }
}
