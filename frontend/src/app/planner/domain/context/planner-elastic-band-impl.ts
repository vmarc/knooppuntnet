import { Coordinate } from '@api/custom/coordinate';
import { GeoJSONSource } from 'maplibre-gl';
import { Map as MaplibreMap } from 'maplibre-gl';
import { PlannerElasticBand } from './planner-elastic-band';

export class PlannerElasticBandImpl implements PlannerElasticBand {
  private static SOURCE_ID = 'planner-elastic-band-source';
  private static LAYER_ID = 'planner-elastic-band-layer';
  private static LINE1_ID = 'planner-elastic-band-line1';
  private static LINE2_ID = 'planner-elastic-band-line2';

  private _map: MaplibreMap;
  private anchor1: Coordinate;
  private anchor2: Coordinate;

  addToMap(map: MaplibreMap) {
    this._map = map;
    this.initSource();
  }

  private initSource(): void {
    this._map.addSource(PlannerElasticBandImpl.SOURCE_ID, {
      type: 'geojson',
      data: {
        type: 'FeatureCollection',
        features: [
          {
            type: 'Feature',
            geometry: {
              type: 'LineString',
              coordinates: [
                [0, 0],
                [0, 0],
              ],
            },
            id: PlannerElasticBandImpl.LINE1_ID,
            properties: {},
          },
          {
            type: 'Feature',
            geometry: {
              type: 'LineString',
              coordinates: [
                [0, 0],
                [0, 0],
              ],
            },
            id: PlannerElasticBandImpl.LINE2_ID,
            properties: {},
          },
        ],
      },
    });
    this._map.addLayer({
      id: PlannerElasticBandImpl.LAYER_ID,
      type: 'line',
      source: PlannerElasticBandImpl.SOURCE_ID,
      layout: {
        visibility: 'none',
      },
      paint: {
        'line-color': 'rgba(0, 0, 255, 0.7)',
        'line-width': 2,
        'line-dasharray': [3, 3],
      },
    });
  }

  set(anchor1: Coordinate, anchor2: Coordinate, position: Coordinate): void {
    this.anchor1 = anchor1;
    this.anchor2 = anchor2;
    this.updatePosition(position);
    this.updateVisibility(true);
  }

  updatePosition(position: Coordinate): void {
    const source: GeoJSONSource = this._map.getSource(PlannerElasticBandImpl.SOURCE_ID);
    source.updateData({
      update: [
        {
          id: PlannerElasticBandImpl.LINE1_ID,
          newGeometry: {
            type: 'LineString',
            coordinates: [this.anchor1, position],
          },
        },
        {
          id: PlannerElasticBandImpl.LINE2_ID,
          newGeometry: {
            type: 'LineString',
            coordinates: [this.anchor2, position],
          },
        },
      ],
    });
  }

  setInvisible() {
    this.updateVisibility(false);
  }

  private updateVisibility(visble: boolean) {
    this._map.setLayoutProperty(
      PlannerElasticBandImpl.LAYER_ID,
      'visibility',
      visble ? 'visible' : 'none'
    );
  }
}
