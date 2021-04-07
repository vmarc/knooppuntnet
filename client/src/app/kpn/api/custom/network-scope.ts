import { List } from 'immutable';

export class NetworkScope {
  static local: NetworkScope = new NetworkScope('local', 'l');
  static regional: NetworkScope = new NetworkScope('regional', 'r');
  static national: NetworkScope = new NetworkScope('national', 'n');
  static international: NetworkScope = new NetworkScope('international', 'i');

  static all: List<NetworkScope> = List([
    NetworkScope.local,
    NetworkScope.regional,
    NetworkScope.national,
    NetworkScope.international,
  ]);

  private constructor(readonly name: string, readonly letter: string) {}

  static fromJSON(jsonObject: any): NetworkScope {
    if (!jsonObject) {
      return undefined;
    }
    return this.withName(jsonObject);
  }

  static withName(name: string): NetworkScope {
    return NetworkScope.all.find((n) => n.name === name);
  }
}
