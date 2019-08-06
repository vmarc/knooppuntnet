import {Component} from "@angular/core";

@Component({
  selector: "kpn-base-sidebar",
  template: `
    <kpn-sidebar-version-warning></kpn-sidebar-version-warning>
    <kpn-sidebar-footer></kpn-sidebar-footer>
  `
})
export class BaseSidebarComponent {
}
