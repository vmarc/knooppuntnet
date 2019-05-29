import {Component} from "@angular/core";

@Component({
  selector: "kpn-poi-group-sports",
  template: `
    <kpn-poi-group name="sports" title="Sports" i18n-title="@@poi.group.sports">
      <kpn-poi-config poiId="american-football"></kpn-poi-config>
      <kpn-poi-config poiId="baseball"></kpn-poi-config>
      <kpn-poi-config poiId="basketball"></kpn-poi-config>
      <kpn-poi-config poiId="cycling"></kpn-poi-config>
      <kpn-poi-config poiId="gymnastics"></kpn-poi-config>
      <kpn-poi-config poiId="golf"></kpn-poi-config>
      <kpn-poi-config poiId="hockey"></kpn-poi-config>
      <kpn-poi-config poiId="horseracing"></kpn-poi-config>
      <kpn-poi-config poiId="icehockey"></kpn-poi-config>
      <kpn-poi-config poiId="soccer"></kpn-poi-config>
      <kpn-poi-config poiId="sportscentre"></kpn-poi-config>
      <kpn-poi-config poiId="surfing"></kpn-poi-config>
      <kpn-poi-config poiId="swimming"></kpn-poi-config>
      <kpn-poi-config poiId="tennis"></kpn-poi-config>
      <kpn-poi-config poiId="volleyball"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupSportsComponent {
}
