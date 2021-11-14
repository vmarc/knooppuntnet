export class LocationFlatNode {
  constructor(
    public expandable: boolean,
    public name: string,
    public localName: string,
    public nodeCount: number,
    public level: number
  ) {}
}
