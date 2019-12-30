import Coordinate from "ol/coordinate";
import {PoiId} from "../../../components/ol/domain/poi-id";

export interface PlannerOverlay {

  setPosition(coordinate: Coordinate, poiId: PoiId): void;

}
