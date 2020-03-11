import {Coordinate} from "ol/coordinate";
import {PoiClick} from "../../../components/ol/domain/poi-click";

export interface PlannerOverlay {

  poiClicked(poiClick: PoiClick): void;

  setPosition(coordinate: Coordinate): void;

}
