import {Injectable} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {AppService} from "../app.service";
import {InterpretedPoiConfiguration} from "./domain/interpreted-poi-configuration";

@Injectable()
export class MapService {

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
