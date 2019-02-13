import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {NetworkType} from "../../../kpn/shared/network-type";
import {MapService} from "../../../components/ol/map.service";

@Component({
  selector: 'kpn-map-main-page',
  template: `
    <kpn-map
      content
      id="main-map"
      class="map"
      [networkType]="networkType">
    </kpn-map>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
    }
  `]
})
export class MapMainPageComponent implements OnInit, OnDestroy {

  networkType: NetworkType;
  paramsSubscription: Subscription;
  selectedFeatureSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute,
              private mapService: MapService) {
  }

  ngOnInit() {
    this.paramsSubscription = this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      const networkTypeName = params['networkType'];
      this.networkType = new NetworkType(networkTypeName);
    });

    this.selectedFeatureSubscription = this.mapService.selectedFeature.subscribe(selectedFeature => {
      console.log("DEBUG MapMainPageComponent type=" + selectedFeature.featureType + ", id=" + selectedFeature.featureId + ", name=" + selectedFeature.name);
    });
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
    this.selectedFeatureSubscription.unsubscribe();
  }
}
