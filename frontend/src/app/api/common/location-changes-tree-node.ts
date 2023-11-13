// this file is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';

export interface LocationChangesTreeNode {
  readonly locationName: string;
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly children: LocationChangesTreeNode[];
  readonly happy: boolean;
  readonly investigate: boolean;
}
