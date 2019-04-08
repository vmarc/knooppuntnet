import {List, Map} from "immutable";
import Coordinate from 'ol/coordinate';
import {PlannerRouteLayer} from "./planner-route-layer";

export class PlannerRouteLayerMock implements PlannerRouteLayer {

  private routeLegs: Map<string, List<Coordinate>> = Map();
  private startNodeFlags: Map<string, Coordinate> = Map();
  private viaNodeFlags: Map<string, Coordinate> = Map();

  // ---

  addStartNodeFlag(nodeId: string, coordinate: Coordinate): void {
    this.startNodeFlags = this.startNodeFlags.set(nodeId, coordinate);
  }

  removeStartNodeFlag(nodeId: string): void {
    this.startNodeFlags = this.viaNodeFlags.remove(nodeId);
  }

  startNodeCount(): number {
    return this.startNodeFlags.size;
  }

  expectStartNodeExists(nodeId): void {
    expect(this.startNodeFlags.get(nodeId)).toBeDefined();
  }

  // ---

  addViaNodeFlag(legId: string, nodeId: string, coordinate: Coordinate): void {
    this.viaNodeFlags = this.viaNodeFlags.set(legId + "-" + nodeId, coordinate);
  }

  removeViaNodeFlag(legId: string, nodeId: string): void {
    this.viaNodeFlags = this.viaNodeFlags.remove(legId + "-" + nodeId);
  }

  viaNodeCount(): number {
    return this.viaNodeFlags.size;
  }

  expectViaNodeExists(legId: string, nodeId: string): void {
    expect(this.viaNodeFlags.get(legId + "-" + nodeId)).toBeDefined();
  }

  // --

  updateFlagPosition(featureId: string, coordinate: Coordinate): void {
    // TODO
  }

  addRouteLeg(legId: string, coordinates: List<Coordinate>): void {
    this.routeLegs = this.routeLegs.set(legId, coordinates);
  }

  removeRouteLeg(legId: string): void {
    this.routeLegs = this.routeLegs.remove(legId);
  }

  routeLegCount(): number {
    return this.routeLegs.size;
  }

  expectRouteLegExists(legId): void {
    expect(this.routeLegs.get(legId)).toBeDefined();
  }

}
