// this class is generated, please do not modify

import { List } from 'immutable';
import { RouteLegNode } from './route-leg-node';
import { RouteLegSegment } from './route-leg-segment';

export class RouteLegRoute {
  constructor(
    readonly source: RouteLegNode,
    readonly sink: RouteLegNode,
    readonly meters: number,
    readonly segments: List<RouteLegSegment>,
    readonly streets: List<string>
  ) {}

  public static fromJSON(jsonObject: any): RouteLegRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLegRoute(
      RouteLegNode.fromJSON(jsonObject.source),
      RouteLegNode.fromJSON(jsonObject.sink),
      jsonObject.meters,
      jsonObject.segments
        ? List(
            jsonObject.segments.map((json: any) =>
              RouteLegSegment.fromJSON(json)
            )
          )
        : List(),
      jsonObject.streets ? List(jsonObject.streets) : List()
    );
  }
}
