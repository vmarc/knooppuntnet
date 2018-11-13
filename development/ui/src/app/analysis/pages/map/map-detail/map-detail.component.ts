import {Component, Input} from '@angular/core';
import {SelectedFeature} from "../../../../map/domain/selected-feature";
import {NetworkType} from "../../../../kpn/shared/network-type";

@Component({
  selector: 'kpn-map-detail',
  templateUrl: './map-detail.component.html',
  styleUrls: ['./map-detail.component.scss']
})
export class MapDetailComponent {

  @Input() selectedFeature: SelectedFeature;
  @Input() networkType: NetworkType;

  isDefault(): boolean {
    return !(this.isNodeSelected() || this.isRouteSelected());
  }

  isNodeSelected(): boolean {
    return this.selectedFeature && this.selectedFeature.featureType === "node";
  }

  isRouteSelected(): boolean {
    return this.selectedFeature && this.selectedFeature.featureType === "route";
  }

}
