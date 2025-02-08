import { RouteScope } from '@api/common/route-scope';

export class RouteScopes {
  static all: RouteScope[] = ['local', 'regional', 'national', 'international'];

  static letter(routeScope: RouteScope): string {
    if (routeScope === 'local') {
      return 'l';
    }
    if (routeScope === 'regional') {
      return 'r';
    }
    if (routeScope === 'national') {
      return 'n';
    }
    if (routeScope === 'international') {
      return 'i';
    }
    return '?';
  }
}
