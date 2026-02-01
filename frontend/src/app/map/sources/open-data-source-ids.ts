export class OpenDataSourceIds {
  constructor(
    private country: string,
    private routeType: string
  ) {}

  sourceId(): string {
    return `opendata-${this.country}-${this.routeType}`;
  }

  routeLayerId(): string {
    return `opendata-${this.country}-${this.routeType}-route`;
  }

  nodeLayerId(): string {
    return `opendata-${this.country}-${this.routeType}-node`;
  }

  nodeNameLayerId(): string {
    return `opendata-${this.country}-${this.routeType}-nodename`;
  }

  virtualRouteLayerId(): string {
    return `opendata-${this.country}-${this.routeType}-virtual-route`;
  }

  virtualNodeLayerId(): string {
    return `opendata-${this.country}-${this.routeType}-virtual-node`;
  }
}
