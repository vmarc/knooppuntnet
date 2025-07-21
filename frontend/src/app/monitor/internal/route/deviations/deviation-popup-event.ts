import { MonitorRouteDeviationInfo } from '@api/common/monitor/monitor-route-deviation-info';

export interface DeviationPopupEvent {
  readonly event: MouseEvent;
  readonly deviation: MonitorRouteDeviationInfo;
}
