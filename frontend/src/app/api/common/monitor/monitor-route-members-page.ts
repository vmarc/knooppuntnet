// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { RouteType } from '@api/common/route-type';
import { StructureRow } from '@api/common/route/structure-row';
import { MonitorReferenceType } from './monitor-reference-type';

export interface MonitorRouteMembersPage {
  readonly adminRole: boolean;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly routeId: string;
  readonly relationId: number;
  readonly relationIds: number[];
  readonly referenceType: MonitorReferenceType;
  readonly routeTypes: RouteType[];
  readonly structureRows: StructureRow[];
  readonly bounds?: Bounds;
}
