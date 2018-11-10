import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {SelectedFeature} from "../../../../map/domain/selected-feature";

@Component({
  selector: 'kpn-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.scss']
})
export class MapPageComponent implements OnInit {

  networkType: string;
  selectedFeature: SelectedFeature;

  constructor(private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.networkType = this.activatedRoute.snapshot.paramMap.get('networkType');
  }

  featureSelectionChanged(selectedFeature: SelectedFeature) {
    this.selectedFeature = selectedFeature;
  }

}
