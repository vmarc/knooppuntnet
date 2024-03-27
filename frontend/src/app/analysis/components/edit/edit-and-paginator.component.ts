import { viewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { input } from '@angular/core';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { EditLinkComponent } from './edit-link.component';

@Component({
  selector: 'kpn-edit-and-paginator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="edit-and-paginator">
      <kpn-edit-link (edit)="editClicked()" title="{{ editLinkTitle() }}" />
      <div class="paginator">
        <kpn-paginator
          [pageIndex]="pageIndex()"
          (pageIndexChange)="onPageIndexChange($event)"
          [pageSize]="pageSize()"
          (pageSizeChange)="onPageSizeChange($event)"
          [length]="length()"
          [showPageSizeSelection]="showPageSizeSelection()"
          [showFirstLastButtons]="showFirstLastButtons()"
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
  standalone: true,
  imports: [EditLinkComponent, PaginatorComponent],
})
export class EditAndPaginatorComponent {
  editLinkTitle = input.required<string>();
  pageSize = input.required<number>();
  pageIndex = input<number>();
  length = input.required<number>();
  showFirstLastButtons = input(false);
  showPageSizeSelection = input(false);

  @Output() pageSizeChange = new EventEmitter<number>();
  @Output() pageIndexChange = new EventEmitter<number>();
  @Output() edit = new EventEmitter<void>();

  readonly paginator = viewChild(PaginatorComponent);

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
