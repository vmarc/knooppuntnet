import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-map-sidebar",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-map-sidebar-planner></kpn-map-sidebar-planner>
    <mat-divider></mat-divider>
    <!--
      <kpn-elevation-profile></kpn-elevation-profile>
      <kpn-map-sidebar-appearance></kpn-map-sidebar-appearance>
      <kpn-map-sidebar-legend></kpn-map-sidebar-legend>
    -->
    <kpn-map-sidebar-poi-configuration></kpn-map-sidebar-poi-configuration>
  `
})
export class MapSidebarComponent {
}
