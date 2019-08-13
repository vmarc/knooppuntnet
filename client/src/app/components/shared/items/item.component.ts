import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-item",
  template: `
    <div class="item">
      <div class="item-left">
        {{index + 1}}
      </div>
      <div class="item-right">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styles: [`
    .item {
      border-bottom-color: lightgray;
      border-bottom-style: solid;
      border-bottom-width: 1px;
    }

    .item-right {
      display: table-cell;
      padding: 10px;
      /*
        media.minWidth((PageWidth.SmallMaxWidth + 1).px)(*/
      border-left-color: lightgray;
      border-left-style: solid;
      border-left-width: 1px;
      /*        )
			*/
    }

    .item-left {
      /*
        media.maxWidth(PageWidth.SmallMaxWidth.px)(
          display.none
        ),
        media.minWidth((PageWidth.SmallMaxWidth + 1).px)(*/
      display: table-cell;
      width: 40px;
      padding: 10px; /* (itemPadding.px) */
      /*        )
			*/
    }
  `]
})
export class ItemComponent {

  @Input() index: number;

}
