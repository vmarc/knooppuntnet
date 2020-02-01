import {Component, Input} from "@angular/core";
import {OnInit} from "@angular/core";

@Component({
  selector: "kpn-osm-link-relation",
  template: `
    <kpn-osm-link kind="relation" [id]="relationId" [title]="title"></kpn-osm-link>
  `
})
export class OsmLinkRelationComponent implements OnInit {
  @Input() relationId: number;
  @Input() title = "osm";

  ngOnInit(): void {
    console.log("OsmLinkRelationComponent relationId=" + this.relationId + ", title=" + this.title);
  }
}
