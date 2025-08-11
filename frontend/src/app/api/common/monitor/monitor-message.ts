// this file is generated, please do not modify

import { MonitorCommand } from './monitor-command';

export interface MonitorMessage {
  readonly commands: MonitorCommand[];
  readonly errors?: string[];
  readonly exception?: string;
}
