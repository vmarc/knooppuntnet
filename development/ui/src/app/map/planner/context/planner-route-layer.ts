import {List} from "immutable";
import Coordinate from 'ol/coordinate';
import Feature from 'ol/Feature';

export interface PlannerRouteLayer {

  addStartNodeFlag(nodeId: string, coordinate: Coordinate): void;

  addViaNodeFlag(legId: string, nodeId: string, coordinate: Coordinate): void;

  removeStartNodeFlag(nodeId: string);

  removeViaNodeFlag(legId: string, nodeId: string);

  updateFlagPosition(featureId: string, coordinate: Coordinate): void;

  addRouteLeg(legId: string, coordinates: List<Coordinate>): void;

  removeRouteLeg(legId: string): void;

}
