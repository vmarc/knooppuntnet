import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-josm-link",
  template: `
    <a rel="nofollow" [href]="href()" i18n="@@links.edit">edit</a>
  `
})
export class JosmLinkComponent {

  @Input() kind: string;
  @Input() elementId: number;
  @Input() full = false;

  href(): string {
    const url = "http://localhost:8111/import?url=https://api.openstreetmap.org/api/0.6";
    return `${url}/${this.kind}/${this.elementId}${this.full ? "/full" : ""}`;
  }

}
