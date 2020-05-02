import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-poi-group-shops",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group name="shops" title="Shops" i18n-title="@@poi.group.shops">
      <kpn-poi-config poiId="beauty"></kpn-poi-config>
      <kpn-poi-config poiId="books-stationary"></kpn-poi-config>
      <kpn-poi-config poiId="car"></kpn-poi-config>
      <kpn-poi-config poiId="chemist"></kpn-poi-config>
      <kpn-poi-config poiId="clothes"></kpn-poi-config>
      <kpn-poi-config poiId="copyshop"></kpn-poi-config>
      <kpn-poi-config poiId="cosmetics"></kpn-poi-config>
      <kpn-poi-config poiId="departmentstore"></kpn-poi-config>
      <kpn-poi-config poiId="diy-hardware"></kpn-poi-config>
      <kpn-poi-config poiId="garden-centre"></kpn-poi-config>
      <kpn-poi-config poiId="general"></kpn-poi-config>
      <kpn-poi-config poiId="gift"></kpn-poi-config>
      <kpn-poi-config poiId="hairdresser"></kpn-poi-config>
      <kpn-poi-config poiId="jewelry"></kpn-poi-config>
      <kpn-poi-config poiId="kiosk"></kpn-poi-config>
      <kpn-poi-config poiId="leather"></kpn-poi-config>
      <kpn-poi-config poiId="marketplace"></kpn-poi-config>
      <kpn-poi-config poiId="musical-instrument"></kpn-poi-config>
      <kpn-poi-config poiId="optician"></kpn-poi-config>
      <kpn-poi-config poiId="pets"></kpn-poi-config>
      <kpn-poi-config poiId="phone"></kpn-poi-config>
      <kpn-poi-config poiId="photo"></kpn-poi-config>
      <kpn-poi-config poiId="shoes"></kpn-poi-config>
      <kpn-poi-config poiId="shoppingcentre"></kpn-poi-config>
      <kpn-poi-config poiId="textiles"></kpn-poi-config>
      <kpn-poi-config poiId="toys"></kpn-poi-config>
      <kpn-poi-config poiId="travelagency"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupShopsComponent {
}
