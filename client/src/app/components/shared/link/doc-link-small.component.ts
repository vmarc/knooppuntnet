import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-doc-link-small",
  template: `
    <a [href]="href()" target="knooppuntnet-documentation" title="Go to documentation" class="help-link">
      <div class="help">?</div>
    </a>
  `,
  styles: [`

    .help-link {
      display: inline-block;
      border-color: lightgray;
      color: lightgray;
      border-radius: 50%;
      border-style: solid;
      border-width: 1px;
      width: 20px;
      height: 20px;
    }

    .help {
      font-size: 15px;
      width: 20px;
      margin-top: 1px;
      text-align: center;
    }

  `]
})
export class DocLinkSmallComponent {

  @Input() subject;

  href(): string {
    return `docs/en.html#${this.subject}`;
  }
}
