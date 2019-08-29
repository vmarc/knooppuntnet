import {Component, EventEmitter, Input, Output} from "@angular/core";
import {MatSlideToggleChange, PageEvent} from "@angular/material";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";

@Component({
  selector: "kpn-changes",
  template: `

    <mat-slide-toggle [checked]="parameters.impact" (change)="impactChanged($event)">Impact</mat-slide-toggle>

    <mat-paginator
      (page)="pageChanged($event)"
      [pageIndex]="parameters.pageIndex"
      [pageSize]="parameters.itemsPerPage"
      [pageSizeOptions]="[5, 25, 50, 100, 250, 1000]"
      [length]="totalCount"
      [showFirstLastButtons]="showFirstLastButtons">
    </mat-paginator>

    <div *ngIf="totalCount == 0" i18n="@@changes.no-changes">
      No changes
    </div>

    <div *ngIf="changeCount > 0">

      <ng-content></ng-content>

      <mat-paginator
        (page)="bottomPageChanged($event)"
        [pageIndex]="parameters.pageIndex"
        [pageSize]="parameters.itemsPerPage"
        [length]="totalCount"
        [hidePageSize]="true">
      </mat-paginator>
    </div>
  `
})
export class ChangesComponent {

  @Input() changeCount: number;
  @Input() totalCount: number;
  @Input() showFirstLastButtons = true;

  @Input() parameters: ChangesParameters;
  @Output() parametersChange = new EventEmitter<ChangesParameters>();

  impactChanged(event: MatSlideToggleChange) {
    this.parametersChange.emit({...this.parameters, impact: event.checked, pageIndex: 0});
  }

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this.parametersChange.emit({...this.parameters, pageIndex: event.pageIndex, itemsPerPage: event.pageSize});
  }

  bottomPageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this.pageChanged(event);
  }

}
