import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-page-header",
  template: `
    <div class="header">
      <h1>
        <ng-content></ng-content>
      </h1>
      <kpn-doc-link [subject]="subject"></kpn-doc-link>
    </div>
  `,
  styles: [`

    .header {
      display: flex;
    }

    .header h1 {
      display: inline-block;
      flex: 1;
    }

  `]
})
export class PageHeaderComponent {
  @Input() subject: string;
  @Input() title: string;
}
