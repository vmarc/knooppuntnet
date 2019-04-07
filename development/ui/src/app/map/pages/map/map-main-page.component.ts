import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";
import {MapComponent} from "../../../components/ol/map.component";
import {MapService} from "../../../components/ol/map.service";
import {PageService} from "../../../components/shared/page.service";
import {NetworkType} from "../../../kpn/shared/network-type";

@Component({
  selector: 'kpn-map-main-page',
  template: `
    <kpn-map id="main-map" class="map"></kpn-map>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 48px;
      left: 0;
      right: 0;
      bottom: 0;
    }
  `]
})
export class MapMainPageComponent implements OnInit, OnDestroy {

  paramsSubscription: Subscription;
  selectedFeatureSubscription: Subscription;

  private lastKnownSidebarOpen = false;

  @ViewChild(MapComponent) map: MapComponent;

  constructor(private activatedRoute: ActivatedRoute,
              private pageService: PageService,
              private mapService: MapService) {
  }

  ngOnInit() {
    this.lastKnownSidebarOpen = this.pageService.sidebarOpen.value;

    this.paramsSubscription = this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      const networkTypeName = params['networkType'];
      this.mapService.networkType.next(new NetworkType(networkTypeName));
    });

    this.selectedFeatureSubscription = this.mapService.selectedFeature.subscribe(selectedFeature => {
      if (selectedFeature == null) {
        console.log("DEBUG MapMainPageComponent selectedFeature null");
      } else {
        console.log("DEBUG MapMainPageComponent selectedFeature type=" + selectedFeature.featureType + ", id=" + selectedFeature.featureId + ", name=" + selectedFeature.name);
      }
    });
  }

  ngAfterContentChecked() {
    if (this.lastKnownSidebarOpen !== this.pageService.sidebarOpen.value) {
      this.lastKnownSidebarOpen = this.pageService.sidebarOpen.value;
      setTimeout(() => this.map.updateSize(), 500);
    }
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
    this.selectedFeatureSubscription.unsubscribe();
  }
}
