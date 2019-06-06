import {TagInformation} from "./tagInformation";

export class Poi {

  layer: string;
  latitude: number;
  longitude: number;
  elementId: number;
  elementType: string;
  tags: TagInformation[] = [];
}
