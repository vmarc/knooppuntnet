import { OsmLayer } from './osm-layer';
import { Grid512Layer } from './grid-512-layer';
import { RouteLayer } from './route-layer';

export class Layers {
  static readonly osmLayer = OsmLayer.build();
  static readonly grid512Layer = Grid512Layer.build();
  static readonly routeLayer = RouteLayer.build();
}
