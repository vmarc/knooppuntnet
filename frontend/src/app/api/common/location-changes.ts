// this file is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';
import { RouteType } from './route-type';

export interface LocationChanges {
  readonly routeType: RouteType;
  readonly locationNames: string[];
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
