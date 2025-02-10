import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NzTableModule } from 'ng-zorro-antd/table';
import { InterpretedTags } from './interpreted-tags';
import { TagValueComponent } from './tag-value.component';

@Component({
  selector: 'kpn-tag-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (tags().isEmpty()) {
      <ng-container i18n="@@tags.no-tags" class="no-tags">No tags</ng-container>
    } @else {
      <nz-table nzBordered [nzFrontPagination]="false" title="tags" nzSize="small" [nzData]="['']">
        <thead>
          <tr>
            <th i18n="@@tags.key">Key</th>
            <th i18n="@@tags.value">Value</th>
          </tr>
        </thead>
        <tbody>
          @for (tag of tags().standardTags(); track tag) {
            <tr>
              <td>{{ tag.key }}</td>
              <td>
                <kpn-tag-value [tag]="tag" />
              </td>
            </tr>
          }
          @if (tags().hasExtraTags() && tags().hasStandardTags()) {
            <tr>
              <td colspan="2"></td>
            </tr>
          }
          @for (tag of tags().extraTags(); track tag) {
            <tr>
              <td>{{ tag.key }}</td>
              <td>
                <kpn-tag-value [tag]="tag" />
              </td>
            </tr>
          }
        </tbody>
      </nz-table>
    }
  `,
  styles: `
    :host {
      display: inline-block;
    }

    .no-tags {
      padding-top: 10px;
      padding-bottom: 10px;
    }
  `,
  imports: [TagValueComponent, NzTableModule],
})
export class TagTableComponent {
  tags = input.required<InterpretedTags>();
}
