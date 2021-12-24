// this file is generated, please do not modify

import { Location } from './location/location';
import { Tags } from '../custom/tags';

export interface Poi {
  readonly _id: string;
  readonly elementType: string;
  readonly elementId: number;
  readonly latitude: string;
  readonly longitude: string;
  readonly layers: string[];
  readonly tags: Tags;
  readonly location: Location;
  readonly tiles: string[];
}
