import {Component} from "@angular/core";

@Component({
  selector: "kpn-poi-group-tourism",
  template: `
    <kpn-poi-group name="tourism" title="Tourism" i18n-title="@@poi.group.tourism">
      <kpn-poi-config poiId="arts-centre"></kpn-poi-config>
      <kpn-poi-config poiId="artwork"></kpn-poi-config>
      <kpn-poi-config poiId="casino"></kpn-poi-config>
      <kpn-poi-config poiId="gallery"></kpn-poi-config>
      <kpn-poi-config poiId="monumental-tree"></kpn-poi-config>
      <kpn-poi-config poiId="museum"></kpn-poi-config>
      <kpn-poi-config poiId="vineyard"></kpn-poi-config>
      <kpn-poi-config poiId="tourism"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupTourismComponent {
}
