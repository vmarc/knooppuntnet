import {Component} from '@angular/core';
import {MapService, PoiId} from "../../components/ol/map.service";
import {AppService} from "../../app.service";
import {Tags} from "../../kpn/shared/data/tags";

@Component({
  selector: 'kpn-poi-detail',
  template: `
    <osm-link kind="{{poiId.elementType}}" id="{{poiId.elementId}}" title="osm"></osm-link>
    <josm-link kind="{{poiId.elementType}}" id="{{poiId.elementId}}" title="edit"></josm-link>

    <br/>
    latitude {{latitude}}
    <br/>
    longitude {{longitude}}
    <br/>

    <tags [tags]="tags"></tags>
  `
})
export class PoiDetailComponent {

  poiId: PoiId;
  tags: Tags;
  latitude: string;
  longitude: string;

  constructor(private mapService: MapService,
              private appService: AppService) {
    mapService.poiClicked.subscribe(poiId => {
      this.appService.poi(poiId.elementType, poiId.elementId).subscribe(response => {
        this.latitude = response.result.latitude;
        this.longitude = response.result.longitude;
        this.tags = response.result.tags;
      });
      this.poiId = poiId;
    });
  }

}
