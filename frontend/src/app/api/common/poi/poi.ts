// this file is generated, please do not modify

import { Location } from '@api/common/location/location';
import { Tag } from '@api/custom/tag';

export interface Poi {
  readonly _id: string;
  readonly elementType: string;
  readonly elementId: number;
  readonly latitude: string;
  readonly longitude: string;
  readonly layers: ReadonlyArray<string>;
  readonly tags: ReadonlyArray<Tag>;
  readonly location: Location;
  readonly tiles: ReadonlyArray<string>;
  readonly description?: string;
  readonly address?: string;
  readonly link: boolean;
  readonly image: boolean;
}
