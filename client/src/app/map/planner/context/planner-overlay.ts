import {PoiClick} from "../../../components/ol/domain/poi-click";

export interface PlannerOverlay {

  poiClicked(poiClick: PoiClick): void;

  setPosition(poiClick: PoiClick): void;

}
