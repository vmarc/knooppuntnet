import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-doc-link",
  template: `
    <a [href]="href()" target="knooppuntnet-documentation" title="Go to documentation">
      <button mat-mini-fab>
        <mat-icon>?</mat-icon>
      </button>
    </a>    
  `,
  styles: [`
    .mat-mini-fab.mat-accent {
      background-color: #eeeeee;
      color: gray;
    }

  `]
})
export class DocLinkComponent {

  @Input() subject;

  href(): string {
    return `docs/html/en_user.html#${this.subject}`;
  }
}
