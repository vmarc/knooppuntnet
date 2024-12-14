import { NetworkScope } from '@api/common';

export class NetworkScopes {
  static all: NetworkScope[] = ['local', 'regional', 'national', 'international'];

  static withName(name: string): NetworkScope {
    return NetworkScopes.all.find((networkScope) => networkScope === name);
  }

  static letter(networkScope: NetworkScope): string {
    if (networkScope === 'local') {
      return 'l';
    }
    if (networkScope === 'regional') {
      return 'r';
    }
    if (networkScope === 'national') {
      return 'n';
    }
    if (networkScope === 'international') {
      return 'i';
    }
    return '?';
  }
}
