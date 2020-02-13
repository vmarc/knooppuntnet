import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-osm-link",
  template: `
    <a
      class="external"
      rel="nofollow"
      target="_blank"
      href="https://www.openstreetmap.org/{{kind}}/{{elementId}}">
      {{title}}
    </a>
  `
})
export class OsmLinkComponent {

  @Input() kind: string;
  @Input() elementId: string;
  @Input() title: string;

}
