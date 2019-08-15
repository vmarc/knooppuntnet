import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-josm-link",
  template: `
    <a rel="nofollow" [href]="href()">edit</a>
  `
})
export class JosmLinkComponent {
  @Input() kind = "";
  @Input() id = "";
  @Input() full = false;

  href(): string {
    return `http://localhost:8111/import?url=https://api.openstreetmap.org/api/0.6/${this.kind}/${this.id}${this.full ? "/full" : ""}`;
  }

}
