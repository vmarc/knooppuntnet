export class RouteSourceIds {
  constructor(private routeType: string) {}

  sourceId(): string {
    return `route-${this.routeType}`;
  }

  layerIds(): string[] {
    return [
      this.routeLayerId(),
      this.nodeRouteLayerId(),
      this.nodeRouteArrowLayerId(),
      this.nodeLayerId(),
      this.nodeNameLayerId(),
      this.routeSurfaceLayerId(),
      this.nodeRouteSurfaceLayerId(),
    ];
  }

  routeLayerId(): string {
    return `route-${this.routeType}-route`;
  }

  nodeRouteLayerId(): string {
    return `route-${this.routeType}-node-route`;
  }

  nodeRouteArrowLayerId(): string {
    return `route-${this.routeType}-node-route-arrow`;
  }

  nodeLayerId(): string {
    return `route-${this.routeType}-node`;
  }

  nodeFocusLayerId(): string {
    return `route-${this.routeType}-node-focus`;
  }

  nodeNameLayerId(): string {
    return `route-${this.routeType}-node-name`;
  }

  routeSurfaceLayerId(): string {
    return `route-${this.routeType}-route-surface`;
  }

  nodeRouteSurfaceLayerId(): string {
    return `route-${this.routeType}-node-route-surface`;
  }
}
