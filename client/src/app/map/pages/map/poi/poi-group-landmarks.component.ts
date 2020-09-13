import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-poi-group-landmarks",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group name="landmarks" title="Landmarks" i18n-title="@@poi.group.landmarks">
      <kpn-poi-config poiId="windmill"></kpn-poi-config>
      <kpn-poi-config poiId="watermill"></kpn-poi-config>
      <kpn-poi-config poiId="place-of-worship"></kpn-poi-config>
      <kpn-poi-config poiId="church"></kpn-poi-config>
      <kpn-poi-config poiId="mosque"></kpn-poi-config>
      <kpn-poi-config poiId="buddhist-temple"></kpn-poi-config>
      <kpn-poi-config poiId="hindu-temple"></kpn-poi-config>
      <kpn-poi-config poiId="synagogue"></kpn-poi-config>
      <kpn-poi-config poiId="wayside-shrine"></kpn-poi-config>
      <kpn-poi-config poiId="heritage"></kpn-poi-config>
      <kpn-poi-config poiId="historic"></kpn-poi-config>
      <kpn-poi-config poiId="boundary-stone"></kpn-poi-config>
      <kpn-poi-config poiId="castle"></kpn-poi-config>
      <kpn-poi-config poiId="monument-memorial"></kpn-poi-config>
      <kpn-poi-config poiId="statue"></kpn-poi-config>
      <kpn-poi-config poiId="zoo"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupLandmarksComponent {
}
