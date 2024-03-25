export class LocationFlatNode {
  constructor(
    public expandable: boolean,
    public path: string,
    public name: string,
    public nodeCount: number,
    public level: number
  ) {}
}
