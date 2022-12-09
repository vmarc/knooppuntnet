import { MonitorRouteRelation } from '@api/common/monitor/monitor-route-relation';

export interface MonitorRouteRelationRow {
  readonly level: number;
  readonly name: string;
  readonly distance: string;
  readonly relation: MonitorRouteRelation;
}
