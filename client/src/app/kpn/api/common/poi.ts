// this file is generated, please do not modify

import { Tags } from '@api/custom';
import { Location } from './location';

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
  readonly description: string;
  readonly address: string;
  readonly link: boolean;
  readonly image: boolean;
}
