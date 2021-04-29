import { NetworkScope } from '@api/custom/network-scope';

export class NetworkScopes {
  static all: NetworkScope[] = [
    NetworkScope.local,
    NetworkScope.regional,
    NetworkScope.national,
    NetworkScope.international,
  ];

  static withName(name: string): NetworkScope {
    return NetworkScopes.all.find((networkScope) => networkScope === name);
  }

  static letter(networkScope: NetworkScope): string {
    if (networkScope === NetworkScope.local) {
      return 'l';
    }
    if (networkScope === NetworkScope.regional) {
      return 'r';
    }
    if (networkScope === NetworkScope.national) {
      return 'n';
    }
    if (networkScope === NetworkScope.international) {
      return 'i';
    }
    return '?';
  }
}
