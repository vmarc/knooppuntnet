import { Component, OnInit, ChangeDetectionStrategy } from "@angular/core";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-map-toolbar",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-plan-actions></kpn-plan-actions>
    <kpn-network-type-selector></kpn-network-type-selector>
  `,
  styles: [
  ]
})
export class MapToolbarComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
