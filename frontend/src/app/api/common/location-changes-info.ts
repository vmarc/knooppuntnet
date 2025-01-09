// this file is generated, please do not modify

import { ChangeSetElementRefs } from './change-set-element-refs';
import { LocationInfo } from './location-info';
import { RouteType } from './route-type';

export interface LocationChangesInfo {
  readonly routeType: RouteType;
  readonly locationInfos: LocationInfo[];
  readonly routeChanges: ChangeSetElementRefs;
  readonly nodeChanges: ChangeSetElementRefs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
