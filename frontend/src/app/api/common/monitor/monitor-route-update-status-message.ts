// this file is generated, please do not modify

import { MonitorRouteUpdateStatusCommand } from './monitor-route-update-status-command';

export interface MonitorRouteUpdateStatusMessage {
  readonly commands: MonitorRouteUpdateStatusCommand[];
  readonly errors: string[] | undefined;
  readonly exception: string;
}
