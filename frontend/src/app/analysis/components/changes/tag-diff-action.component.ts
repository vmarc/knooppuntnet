import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TagDiffType } from '@api/common/diff/tag-diff-type';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-tag-diff-action',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @switch (action()) {
      @case ('add') {
        <nz-icon nzType="add" />
      }
      @case ('update') {
        <nz-icon nzType="sync" />
      }
      @case ('delete') {
        <nz-icon nzType="close" />
      }
    }
  `,
  imports: [NzIconDirective],
})
export class TagDiffActionComponent {
  readonly action = input.required<TagDiffType>();
}
