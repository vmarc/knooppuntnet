import Coordinate from "ol/coordinate";
import {FeatureId} from "../features/feature-id";

export class PlanNode {

  private constructor(public readonly featureId: string,
                      public readonly nodeId: string,
                      public readonly nodeName: string,
                      public readonly coordinate: Coordinate) {
  }

  static create(nodeId: string, nodeName: string, coordinate: Coordinate): PlanNode {
    return new PlanNode(FeatureId.next(), nodeId, nodeName, coordinate);
  }

}
