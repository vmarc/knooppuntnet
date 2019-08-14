import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-osm-link-way",
  template: `
    <kpn-osm-link kind="way" [id]="wayId" [title]="title"></kpn-osm-link>
  `,
  styles: []
})
export class OsmLinkWayComponent {
  @Input() wayId: number;
  @Input() title: string = "osm";
}
