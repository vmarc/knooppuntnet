import { Coordinate } from '@api/custom/coordinate';
import { GeoJSONSource } from 'maplibre-gl';
import { Map as MaplibreMap } from 'maplibre-gl';
import { PlanFlag } from '../plan/plan-flag';
import { PlanFlagType } from '../plan/plan-flag-type';
import { PlannerMarkerLayer } from './planner-marker-layer';

export class PlannerMarkerLayerImpl extends PlannerMarkerLayer {
  static SOURCE_ID = 'planner-marker-source';

  private _map: MaplibreMap;

  addToMap(map: MaplibreMap) {
    this._map = map;
    this.initSource();
  }

  private initSource(): void {
    this._map.addSource(PlannerMarkerLayerImpl.SOURCE_ID, {
      type: 'geojson',
      data: {
        type: 'FeatureCollection',
        features: [],
      },
    });
    this._map.addLayer({
      id: 'planner-marker-layer',
      type: 'symbol',
      source: PlannerMarkerLayerImpl.SOURCE_ID,
      layout: {
        'icon-image': [
          'match',
          ['get', 'flagType'],
          'start',
          'marker-icon-green',
          'end',
          'marker-icon-red',
          'via',
          'marker-icon-orange',
          'invisible',
          'marker-icon-blue',
          'marker-icon-yellow', // default
        ],
        'icon-anchor': 'bottom',
      },
    });

    const coordinate: Coordinate = [4.46839, 51.46774]; // essen
    this.addFlag(new PlanFlag(PlanFlagType.invisible, '12345', coordinate));
  }

  addFlag(flag: PlanFlag): void {
    if (flag && flag.flagType !== PlanFlagType.invisible) {
      const marker: GeoJSON.Feature = {
        type: 'Feature',
        geometry: {
          type: 'Point',
          coordinates: [flag.coordinate[0], flag.coordinate[1]],
        },
        id: flag.featureId,
        properties: {
          layer: 'flag',
          flagType: flag.flagType,
        },
      };
      const source: GeoJSONSource = this._map.getSource(PlannerMarkerLayerImpl.SOURCE_ID);
      source.updateData({
        add: [marker],
      });
    }
  }

  removeFlag(flag: PlanFlag): void {
    if (flag) {
      this.removeFlagWithFeatureId(flag.featureId);
    }
  }

  removeFlagWithFeatureId(featureId: string): void {
    const source: GeoJSONSource = this._map.getSource(PlannerMarkerLayerImpl.SOURCE_ID);
    source.updateData({
      remove: [featureId],
    });
  }

  updateFlag(flag: PlanFlag): void {
    if (flag) {
      this.removeFlagWithFeatureId(flag.featureId);
      this.addFlag(flag);
    }
  }

  updateFlagCoordinate(featureId: string, coordinate: Coordinate): void {
    const source: GeoJSONSource = this._map.getSource(PlannerMarkerLayerImpl.SOURCE_ID);
    source.updateData({
      update: [
        {
          id: featureId,
          newGeometry: {
            type: 'Point',
            coordinates: coordinate,
          },
        },
      ],
    });
  }

  features() /*: Array<Feature<G, P>>*/ {
    throw new Error('Method not implemented.');
    //    return this.featureCollection.features;
  }
}
