import {Map} from "immutable";
import Coordinate from 'ol/coordinate';
import {PlanLeg} from "../plan/plan-leg";
import {PlannerRouteLayer} from "./planner-route-layer";

export class PlannerRouteLayerMock implements PlannerRouteLayer {

  private routeLegs: Map<string, PlanLeg> = Map();
  private startNodeFlags: Map<string, Coordinate> = Map();
  private viaNodeFlags: Map<string, Coordinate> = Map();

  // ---

  addStartNodeFlag(nodeId: string, coordinate: Coordinate): void {
    this.startNodeFlags = this.startNodeFlags.set(nodeId, coordinate);
  }

  removeStartNodeFlag(nodeId: string): void {
    this.startNodeFlags = this.viaNodeFlags.remove(nodeId);
  }

  expectStartNodeCount(count: number): void {
    expect(this.startNodeFlags.size).toEqual(count);
  }

  expectStartNodeExists(nodeId: string, coordinate: Coordinate): void {
    const flagCoordinate = this.startNodeFlags.get(nodeId);
    expect(flagCoordinate).toBeDefined();
    expect(flagCoordinate).toEqual(coordinate);
  }

  // ---

  addViaNodeFlag(legId: string, nodeId: string, coordinate: Coordinate): void {
    this.viaNodeFlags = this.viaNodeFlags.set(legId + "-" + nodeId, coordinate);
  }

  removeViaNodeFlag(legId: string, nodeId: string): void {
    this.viaNodeFlags = this.viaNodeFlags.remove(legId + "-" + nodeId);
  }

  expectViaNodeCount(count: number): void {
    expect(this.viaNodeFlags.size).toEqual(count);
  }

  expectViaNodeExists(legId: string, nodeId: string, coordinate: Coordinate): void {
    const flagCoordinate = this.viaNodeFlags.get(legId + "-" + nodeId);
    expect(flagCoordinate).toBeDefined();
    expect(flagCoordinate).toEqual(coordinate);
  }

  // --

  updateFlagPosition(featureId: string, coordinate: Coordinate): void {
    // TODO
  }

  addRouteLeg(leg: PlanLeg): void {
    this.routeLegs = this.routeLegs.set(leg.legId, leg);
  }

  removeRouteLeg(legId: string): void {
    this.routeLegs = this.routeLegs.remove(legId);
  }

  expectRouteLegCount(count: number): void {
    expect(this.routeLegs.size).toEqual(count);
  }

  expectRouteLegExists(legId: string, leg: PlanLeg): void {
    const routeLeg = this.routeLegs.get(legId);
    expect(routeLeg).toBeDefined();
    expect(routeLeg).toEqual(leg);
  }

}
