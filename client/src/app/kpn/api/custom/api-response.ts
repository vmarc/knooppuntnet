import { Timestamp } from './timestamp';

export interface ApiResponse<T> {
  readonly situationOn?: Timestamp;
  readonly version?: number;
  readonly result?: T;
}
