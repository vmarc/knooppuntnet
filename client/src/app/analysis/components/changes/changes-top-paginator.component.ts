import {Component, EventEmitter, Input, Output} from "@angular/core";
import {PageEvent} from "@angular/material";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";

@Component({
  selector: "kpn-changes-top-paginator",
  template: `
    <mat-paginator
        (page)="pageChanged($event)"
        [pageIndex]="parameters.pageIndex"
        [pageSize]="parameters.itemsPerPage"
        [pageSizeOptions]="[5, 25, 50, 100, 250, 1000]"
        [length]="totalCount"
        [showFirstLastButtons]="true">
    </mat-paginator>
  `
})
export class ChangesTopPaginatorComponent {

  @Input() parameters: ChangesParameters;
  @Input() totalCount: number;
  @Output() pageIndexChanged = new EventEmitter<number>();

  pageChanged(event: PageEvent) {
    this.pageIndexChanged.emit(event.pageIndex);
  }

}
