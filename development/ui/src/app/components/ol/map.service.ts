import {Injectable} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {AppService} from "../../app.service";
import {InterpretedPoiConfiguration} from "./domain/interpreted-poi-configuration";
import {SelectedFeature} from "./domain/selected-feature";

export class PoiId {
  constructor(readonly elementType: string,
              readonly elementId: number) {
  }
}

@Injectable()
export class MapService {

  // former MapState
  highlightedRouteId: string;
  highlightedNodeId: string;
  selectedRouteId: string;
  selectedNodeId: string;

  selectedFeature: ReplaySubject<SelectedFeature> = new ReplaySubject(1);
  poiClicked: ReplaySubject<PoiId> = new ReplaySubject(1);
  poiConfiguration: ReplaySubject<InterpretedPoiConfiguration> = new ReplaySubject(1);

  constructor(private appService: AppService) {
    this.loadPoiConfiguration();
  }

  private loadPoiConfiguration() {
    this.appService.poiConfiguration().subscribe(response => {
      this.poiConfiguration.next(new InterpretedPoiConfiguration(response.result));
    });
  }

}
