import Coordinate from "ol/coordinate";
import {FeatureId} from "../features/feature-id";

export class PlanNode {

  private constructor(readonly featureId: string,
                      readonly nodeId: string,
                      readonly nodeName: string,
                      readonly coordinate: Coordinate) {
  }

  static create(nodeId: string, nodeName: string, coordinate: Coordinate): PlanNode {
    return new PlanNode(FeatureId.next(), nodeId, nodeName, coordinate);
  }

}
