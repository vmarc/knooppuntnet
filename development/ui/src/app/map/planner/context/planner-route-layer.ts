import Coordinate from 'ol/coordinate';
import {PlanLeg} from "../plan/plan-leg";

export interface PlannerRouteLayer {

  addStartNodeFlag(nodeId: string, coordinate: Coordinate): void;

  addViaNodeFlag(legId: string, nodeId: string, coordinate: Coordinate): void;

  removeStartNodeFlag(nodeId: string);

  removeViaNodeFlag(legId: string, nodeId: string);

  updateFlagPosition(featureId: string, coordinate: Coordinate): void;

  addRouteLeg(leg: PlanLeg): void;

  removeRouteLeg(legId: string): void;

}
