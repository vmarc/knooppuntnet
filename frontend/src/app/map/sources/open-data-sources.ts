import { OpenDataSource } from '@app/map/sources/open-data-source';
import { RouteSource } from '@app/map/sources/route-source';
import { Map as MaplibreMap } from 'maplibre-gl';

export class OpenDataSources {
  private flandersOpenDataHiking: OpenDataSource;
  private flandersOpenDataCycling: OpenDataSource;
  private netherlandsOpenDataHiking: OpenDataSource;
  private netherlandsOpenDataCycling: OpenDataSource;
  private franceOpenDataHiking: OpenDataSource;

  constructor(private map: MaplibreMap) {
    this.flandersOpenDataHiking = this.buildOpenDataSource('flanders', 'hiking');
    this.flandersOpenDataCycling = this.buildOpenDataSource('flanders', 'cycling');
    this.netherlandsOpenDataHiking = this.buildOpenDataSource('netherlands', 'hiking');
    this.netherlandsOpenDataCycling = this.buildOpenDataSource('netherlands', 'cycling');
    this.franceOpenDataHiking = this.buildOpenDataSource('france', 'hiking');
  }

  private buildOpenDataSource(country: string, routeType: string): OpenDataSource {
    return new OpenDataSource(this.map, country, routeType);
  }

  remove(): void {
    this.flandersOpenDataHiking.remove();
    this.flandersOpenDataCycling.remove();
    this.netherlandsOpenDataHiking.remove();
    this.netherlandsOpenDataCycling.remove();
    this.franceOpenDataHiking.remove();
  }
}
