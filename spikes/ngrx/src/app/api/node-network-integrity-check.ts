export interface NodeNetworkIntegrityCheck {
  readonly failed: boolean;
  readonly expected: number;
  readonly actual: number;
}
