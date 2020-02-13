import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-josm-way",
  template: `<kpn-josm-link kind="way" [elementId]="wayId"></kpn-josm-link>`
})
export class JosmWayComponent {
  @Input() wayId: number;
}
