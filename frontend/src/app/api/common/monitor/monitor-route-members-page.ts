// this file is generated, please do not modify

import { RouteType } from '@api/common/route-type';
import { StructureRow } from '@api/common/route/structure-row';
import { MonitorReferenceType } from './monitor-reference-type';
import { MonitorRouteSummary } from './monitor-route-summary';

export interface MonitorRouteMembersPage {
  readonly summary: MonitorRouteSummary;
  readonly referenceType: MonitorReferenceType;
  readonly routeTypes: ReadonlyArray<RouteType>;
  readonly structureRows: ReadonlyArray<StructureRow>;
}
