import { OpenDataSource } from '@app/map/sources/open-data-source';
import { RouteSource } from '@app/map/sources/route-source';
import { Map as MaplibreMap } from 'maplibre-gl';

export class Sources {
  private canoeRouteSource: RouteSource;
  private cyclingRouteSource: RouteSource;
  private hikingRouteSource: RouteSource;
  private horseRidingRouteSource: RouteSource;
  private inlineSkatingRouteSource: RouteSource;
  private motorboatRouteSource: RouteSource;
  private mtbRouteSource: RouteSource;

  private flandersOpenDataHiking: OpenDataSource;
  private flandersOpenDataCycling: OpenDataSource;
  private netherlandsOpenDataHiking: OpenDataSource;
  private netherlandsOpenDataCycling: OpenDataSource;
  private franceOpenDataHiking: OpenDataSource;

  constructor(private map: MaplibreMap) {
    this.canoeRouteSource = this.buildRouteSource('canoe');
    this.cyclingRouteSource = this.buildRouteSource('cycling');
    this.hikingRouteSource = this.buildRouteSource('hiking');
    this.horseRidingRouteSource = this.buildRouteSource('horse-riding');
    this.inlineSkatingRouteSource = this.buildRouteSource('inline-skating');
    this.motorboatRouteSource = this.buildRouteSource('motorboat');
    this.mtbRouteSource = this.buildRouteSource('mtb');

    this.flandersOpenDataHiking = this.buildOpenDataSource('flanders', 'hiking');
    this.flandersOpenDataCycling = this.buildOpenDataSource('flanders', 'cycling');
    this.netherlandsOpenDataHiking = this.buildOpenDataSource('netherlands', 'hiking');
    this.netherlandsOpenDataCycling = this.buildOpenDataSource('netherlands', 'cycling');
    this.franceOpenDataHiking = this.buildOpenDataSource('france', 'hiking');
  }

  private buildRouteSource(routeType: string): RouteSource {
    return new RouteSource(this.map, routeType);
  }

  private buildOpenDataSource(country: string, routeType: string): OpenDataSource {
    return new OpenDataSource(this.map, country, routeType);
  }

  remove(): void {
    this.canoeRouteSource.remove();
    this.cyclingRouteSource.remove();
    this.hikingRouteSource.remove();
    this.horseRidingRouteSource.remove();
    this.inlineSkatingRouteSource.remove();
    this.motorboatRouteSource.remove();
    this.mtbRouteSource.remove();

    this.flandersOpenDataHiking.remove();
    this.flandersOpenDataCycling.remove();
    this.netherlandsOpenDataHiking.remove();
    this.netherlandsOpenDataCycling.remove();
    this.franceOpenDataHiking.remove();
  }
}
