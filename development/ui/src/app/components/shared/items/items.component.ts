import {Component} from "@angular/core";

@Component({
  selector: "kpn-items",
  template: `
    <div class="items">
      <ng-content></ng-content>
    </div>
  `,
  styles: [`
    .items {
      margin-top: 20px;
      border-top-color: lightgray;
      border-top-style: solid;
      border-top-width: 1px;
      /*
       media . maxWidth(PageWidth . SmallMaxWidth . px)(
       marginLeft((-UiPage . smallContentsMargin) . px),
       marginRight((-UiPage . smallContentsMargin) . px)
    */
    }

  `]
})
export class ItemsComponent {
}
