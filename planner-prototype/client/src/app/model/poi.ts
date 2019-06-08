import {TagInformation} from "./tag-information";

export class Poi {
  layer: string;
  latitude: number;
  longitude: number;
  elementId: number;
  elementType: string;
  tags: TagInformation[] = [];
}
