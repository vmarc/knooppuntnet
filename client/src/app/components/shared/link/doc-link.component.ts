import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-doc-link",
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
      border-width: 2px;
      width: 40px;
      height: 40px;
    }

    .help {
      font-size: 30px;
      width: 40px;
      margin-top: 2px;
      text-align: center;
    }

  `]
})
export class DocLinkComponent {

  @Input() subject;

  href(): string {
    return `docs/en.html#${this.subject}`;
  }
}
