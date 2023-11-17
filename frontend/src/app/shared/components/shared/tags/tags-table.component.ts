import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { InterpretedTags } from './interpreted-tags';

@Component({
  selector: 'kpn-tags-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container *ngIf="tags.isEmpty()" i18n="@@tags.no-tags" class="no-tags"
      >No tags
    </ng-container>

    <table *ngIf="!tags.isEmpty()" title="tags" class="kpn-table">
      <thead>
        <tr>
          <th i18n="@@tags.key">Key</th>
          <th i18n="@@tags.value">Value</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let tag of tags.standardTags()">
          <td>{{ tag.key }}</td>
          <td>{{ tag.value }}</td>
        </tr>
        <tr *ngIf="tags.hasExtraTags() && tags.hasStandardTags()">
          <td colspan="2"></td>
        </tr>
        <tr *ngFor="let tag of tags.extraTags()">
          <td>{{ tag.key }}</td>
          <td>{{ tag.value }}</td>
        </tr>
      </tbody>
    </table>
  `,
  styles: `
    .no-tags {
      padding-top: 10px;
      padding-bottom: 10px;
    }
  `,
  standalone: true,
  imports: [NgIf, NgFor],
})
export class TagsTableComponent {
  @Input({ required: true }) tags: InterpretedTags;
}
