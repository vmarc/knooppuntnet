import { RouteType } from '@api/common';

export class RouteTypes {
  static all: RouteType[] = [
    'cycling',
    'hiking',
    'horse-riding',
    'motorboat',
    'canoe',
    'inline-skating',
  ];

  static withName(name: string): RouteType | undefined {
    return RouteTypes.all.find((routeType) => routeType === name);
  }

  static letter(routeType: RouteType): string {
    if (routeType === 'cycling') {
      return 'c';
    }

    if (routeType === 'hiking') {
      return 'w';
    }

    if (routeType === 'horse-riding') {
      return 'h';
    }

    if (routeType === 'motorboat') {
      return 'm';
    }

    if (routeType === 'canoe') {
      return 'p';
    }

    if (routeType === 'inline-skating') {
      return 'i';
    }

    return '?';
  }
}
