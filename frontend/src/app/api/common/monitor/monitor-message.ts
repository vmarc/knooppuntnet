// this file is generated, please do not modify

import { MonitorCommand } from './monitor-command';

export interface MonitorMessage {
  readonly commands: ReadonlyArray<MonitorCommand>;
  readonly errors?: ReadonlyArray<string>;
  readonly exception?: string;
}
