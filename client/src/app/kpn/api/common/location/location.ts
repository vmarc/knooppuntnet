// this class is generated, please do not modify

export class Location {
  constructor(readonly names: Array<string>) {}

  static fromJSON(jsonObject: any): Location {
    if (!jsonObject) {
      return undefined;
    }
    return new Location(jsonObject.names);
  }
}
