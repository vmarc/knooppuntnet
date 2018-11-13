import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {SelectedFeature} from "../../../../map/domain/selected-feature";
import {NetworkType} from "../../../../kpn/shared/network-type";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.scss']
})
export class MapPageComponent implements OnInit, OnDestroy {

  networkType: NetworkType;
  selectedFeature: SelectedFeature;
  paramsSubscription: Subscription;

  constructor(private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.paramsSubscription = this.paramsSubscription = this.activatedRoute.params.subscribe(params => {
      const networkTypeName = params['networkType'];
      this.networkType = new NetworkType(networkTypeName);
    });
  }

  featureSelectionChanged(selectedFeature: SelectedFeature) {
    this.selectedFeature = selectedFeature;
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }
}
