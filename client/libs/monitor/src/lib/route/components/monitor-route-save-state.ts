export const initialMonitorRouteSaveState: MonitorRouteSaveState = {
  saveRouteEnabled: false,
  saveRouteStatus: 'todo',
  uploadGpxEnabled: false,
  uploadGpxStatus: 'todo',
  analyzeEnabled: false,
  analyzeStatus: 'todo',
  errors: [],
  done: false,
};

export interface MonitorRouteSaveState {
  readonly saveRouteEnabled: boolean;
  readonly saveRouteStatus: 'todo' | 'busy' | 'done';
  readonly uploadGpxEnabled: boolean;
  readonly uploadGpxStatus: 'todo' | 'busy' | 'done';
  readonly analyzeEnabled: boolean;
  readonly analyzeStatus: 'todo' | 'busy' | 'done';
  readonly errors: string[];
  readonly done: boolean;
}
