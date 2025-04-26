import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { TagDiffType } from '@api/common/diff/tag-diff-type';

@Component({
  selector: 'kpn-tag-diff-action',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @switch (action()) {
      @case ('add') {
        <mat-icon svgIcon="add" />
      }
      @case ('update') {
        <mat-icon svgIcon="update" />
      }
      @case ('delete') {
        <mat-icon svgIcon="remove" />
      }
    }
  `,
  imports: [MatIconModule],
})
export class TagDiffActionComponent {
  action = input.required<TagDiffType>();
}
