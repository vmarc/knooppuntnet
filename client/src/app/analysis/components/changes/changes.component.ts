import {Component, EventEmitter, Input, Output} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {ChangesParameters} from "../../../kpn/api/common/changes/filter/changes-parameters";

@Component({
  selector: "kpn-changes",
  template: `

    <mat-slide-toggle [checked]="parameters.impact" (change)="impactChanged($event)" i18n="@@changes.impact">Impact</mat-slide-toggle>

    <kpn-paginator
      (page)="pageChanged($event)"
      [pageIndex]="parameters.pageIndex"
      [pageSize]="parameters.itemsPerPage"
      [pageSizeOptions]="[5, 25, 50, 100, 250, 1000]"
      [length]="totalCount"
      [showFirstLastButtons]="showFirstLastButtons">
    </kpn-paginator>

    <div *ngIf="totalCount == 0" i18n="@@changes.no-changes">
      No changes
    </div>

    <div *ngIf="changeCount > 0">

      <ng-content></ng-content>

      <kpn-paginator
        (page)="bottomPageChanged($event)"
        [pageIndex]="parameters.pageIndex"
        [pageSize]="parameters.itemsPerPage"
        [length]="totalCount"
        [hidePageSize]="true">
      </kpn-paginator>
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
