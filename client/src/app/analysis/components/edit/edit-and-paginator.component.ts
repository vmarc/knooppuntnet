import { ViewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { PaginatorComponent } from '../../../components/shared/paginator/paginator.component';

@Component({
  selector: 'kpn-edit-and-paginator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="edit-and-paginator">
      <kpn-edit-link (edit)="editClicked()" title="{{ title }}"></kpn-edit-link>
      <div class="paginator">
        <kpn-paginator
          [pageSize]="pageSize"
          (pageSizeChange)="onPageSizeChange($event)"
          [length]="length"
          [showPageSizeSelection]="showPageSizeSelection"
          [showFirstLastButtons]="showFirstLastButtons"
        >
        </kpn-paginator>
      </div>
    </div>
  `,
  styles: [
    `
      .edit-and-paginator {
        display: flex;
        align-items: center;
      }

      .paginator {
        margin-left: auto;
      }
    `,
  ],
})
export class EditAndPaginatorComponent {
  @Input() title: string;
  @Input() pageSize: number;
  @Input() pageIndex: number;
  @Input() length: number;
  @Input() showFirstLastButtons = false;
  @Input() showPageSizeSelection = false;

  @Output() pageSizeChange = new EventEmitter<number>();
  @Output() pageIndexChange = new EventEmitter<number>();
  @Output() edit = new EventEmitter<void>();

  @ViewChild(PaginatorComponent, { static: true })
  paginator: PaginatorComponent;

  editClicked() {
    this.edit.emit();
  }

  onPageSizeChange(pageSize: number) {
    this.pageSizeChange.emit(pageSize);
  }
}
