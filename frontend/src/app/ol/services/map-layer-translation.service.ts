import { Injectable } from '@angular/core';
import { Translations } from '@app/i18n';
import { BackgroundLayer } from '../layers';
import { OsmLayer } from '../layers';
import { MonitorLayer } from '../layers/monitor-layer';
import { MapLayerDefinition } from './map-layer-definition';

@Injectable({
  providedIn: 'root',
})
export class MapLayerTranslationService {
  readonly mapLayerDefinitions: MapLayerDefinition[] = [
    { name: BackgroundLayer.id, translation: '@@map.layer.background' },
    { name: OsmLayer.id, translation: '@@map.layer.osm' },
    { name: MonitorLayer.id, translation: '@@map.layer.monitor' },
    { name: 'cycling', translation: '@@network-type.cycling' },
    { name: 'hiking', translation: '@@network-type.hiking' },
    { name: 'horse-riding', translation: '@@network-type.horse-riding' },
    { name: 'motorboat', translation: '@@network-type.motorboat' },
    { name: 'canoe', translation: '@@network-type.canoe' },
    { name: 'inline-skating', translation: '@@network-type.inline-skating' },
    //
    {
      name: 'network-nodes-cycling-layer',
      translation: '@@network-type.cycling',
    },
    {
      name: 'network-nodes-hiking-layer',
      translation: '@@network-type.hiking',
    },
    {
      name: 'network-nodes-horse-riding-layer',
      translation: '@@network-type.horse-riding',
    },
    {
      name: 'network-nodes-motorboat-layer',
      translation: '@@network-type.motorboat',
    },
    { name: 'network-nodes-canoe-layer', translation: '@@network-type.canoe' },
    {
      name: 'network-nodes-inline-skating-layer',
      translation: '@@network-type.inline-skating',
    },
    //
    {
      name: 'netherlands-hiking',
      translation: '@@map.layer.netherlands-hiking',
    },
    { name: 'flanders-hiking', translation: '@@map.layer.flanders-hiking' },
    { name: 'flanders-cycling', translation: '@@map.layer.flanders-cycling' },
    { name: 'debug-512', translation: '@@map.layer.tile-512-names' },
    { name: 'debug-256', translation: '@@map.layer.tile-256-names' },
    { name: 'location-boundary', translation: '@@map.layer.boundary' },
    { name: 'node-marker-layer', translation: '@@map.layer.node' },
    { name: 'poi-marker-layer', translation: '@@map.layer.poi' },
    { name: 'route-nodes-layer', translation: '@@map.layer.nodes' },
    { name: 'poi-areas-layer', translation: '@@map.layer.poi-areas' },
    { name: 'network-node-markers-layer', translation: '@@map.layer.nodes' },
    { name: 'network-marker-layer', translation: '@@map.layer.networks' },
  ];

  translation(layerName: string): string {
    const layerDefinition = this.mapLayerDefinitions.find(
      (definition) => definition.name === layerName
    );
    if (layerDefinition) {
      const translation = Translations.get(layerDefinition.translation);
      if (translation) {
        return translation;
      }
    }
    return layerName;
  }
}
