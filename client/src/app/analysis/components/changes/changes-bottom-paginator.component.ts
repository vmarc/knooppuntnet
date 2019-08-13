import {Component, EventEmitter, Input, Output} from "@angular/core";
import {PageEvent} from "@angular/material";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";

@Component({
  selector: "kpn-changes-bottom-paginator",
  template: `
    <mat-paginator
        (page)="pageChanged($event)"
        [pageIndex]="parameters.pageIndex"
        [pageSize]="parameters.itemsPerPage"
        [length]="totalCount"
        [hidePageSize]="true">
    </mat-paginator>
  `
})
export class ChangesBottomPaginatorComponent {

  @Input() parameters: ChangesParameters;
  @Input() totalCount: number;
  @Output() pageIndexChanged = new EventEmitter<number>();

  pageChanged(event: PageEvent) {
    this.pageIndexChanged.emit(event.pageIndex);
  }

}
