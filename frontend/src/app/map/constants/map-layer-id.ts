export class MapLayerId {
  // source SourceId.ROUTES
  static ROUTE = 'route';
  static NODE_ROUTE = 'node-route';
  static NODE_ROUTE_ARROWS = 'node-route-arrows';
  static NODE = 'node';
  static NODE_NAME = 'node-name';
  static ROUTE_SURFACE = 'route-surface';
  static NODE_ROUTE_SURFACE = 'node-route-surface';

  static routeLayers: string[] = [
    MapLayerId.ROUTE,
    MapLayerId.NODE_ROUTE,
    MapLayerId.NODE_ROUTE_ARROWS,
    MapLayerId.NODE,
    MapLayerId.NODE_NAME,
    MapLayerId.ROUTE_SURFACE,
    MapLayerId.NODE_ROUTE_SURFACE,
  ];
}
