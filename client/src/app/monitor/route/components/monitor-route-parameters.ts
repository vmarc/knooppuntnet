import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';

export interface MonitorRouteParameters {
  mode: string;
  initialProperties: MonitorRouteProperties;
  properties: MonitorRouteProperties;
  referenceFile: File | null;
}
