import {Coordinates} from "./coordinates";

export class Section {

  startNodeId: number;
  endNodeId: number;
  startNode: string;
  endNode: string;
  meters: number;
  coordinates: Coordinates[];
  waypoints: { [key: number]: number } = {};
}
