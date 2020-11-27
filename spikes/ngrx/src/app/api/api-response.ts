export interface ApiResponse<T> {
  readonly situationOn: string;
  readonly version: number;
  readonly result?: T;
}
