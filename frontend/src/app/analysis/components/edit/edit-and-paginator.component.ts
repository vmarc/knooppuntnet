import { output } from '@angular/core';
import { viewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OldPaginatorComponent } from '@app/shared/components/paginator/old-paginator.component';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { EditLinkComponent } from './edit-link.component';

@Component({
  selector: 'ui-edit-and-paginator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="edit-and-paginator">
      <ui-edit-link (edit)="editClicked()" [title]="editLinkTitle()" />
      <div class="paginator">
        <ui-paginator
          [pageIndex]="pageIndex()"
          (pageIndexChange)="onPageIndexChange($event)"
          [pageSize]="pageSize()"
          (pageSizeChange)="onPageSizeChange($event)"
          [length]="length()"
        />
      </div>
    </div>
  `,
  styles: `
    .edit-and-paginator {
      display: flex;
      align-items: center;
    }

    .paginator {
      margin-left: auto;
    }
  `,
  imports: [EditLinkComponent, PaginatorComponent],
})
export class EditAndPaginatorComponent {
  readonly editLinkTitle = input.required<string>();
  readonly pageSize = input.required<number>();
  readonly pageIndex = input<number>();
  readonly length = input.required<number>();
  readonly showFirstLastButtons = input(false);
  readonly showPageSizeSelection = input(false);

  readonly pageSizeChange = output<number>();
  readonly pageIndexChange = output<number>();
  readonly edit = output<void>();

  readonly paginator = viewChild(OldPaginatorComponent);

  editClicked() {
    this.edit.emit();
  }

  onPageSizeChange(pageSize: number) {
    this.pageSizeChange.emit(pageSize);
  }

  onPageIndexChange(pageIndex: number) {
    this.pageIndexChange.emit(pageIndex);
  }
}
