// this class is generated, please do not modify

import { List } from 'immutable';
import { ChangeSetElementRefs } from './change-set-element-refs';

export class LocationChangesTreeNode {
  constructor(
    readonly locationName: string,
    readonly routeChanges: ChangeSetElementRefs,
    readonly nodeChanges: ChangeSetElementRefs,
    readonly children: List<LocationChangesTreeNode>,
    readonly happy: boolean,
    readonly investigate: boolean
  ) {}

  public static fromJSON(jsonObject: any): LocationChangesTreeNode {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationChangesTreeNode(
      jsonObject.locationName,
      ChangeSetElementRefs.fromJSON(jsonObject.routeChanges),
      ChangeSetElementRefs.fromJSON(jsonObject.nodeChanges),
      jsonObject.children
        ? List(
            jsonObject.children.map((json: any) =>
              LocationChangesTreeNode.fromJSON(json)
            )
          )
        : List(),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
