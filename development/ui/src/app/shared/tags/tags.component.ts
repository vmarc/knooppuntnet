import {Component, Input} from "@angular/core";
import {Tags} from "../../kpn/shared/data/tags";

@Component({
  selector: 'tags',
  template: `
    <ng-container *ngIf="tags.tags.length == 0">No tags</ng-container> <!-- Geen labels -->

    <table *ngIf="tags.tags.length > 0" title="tags" class="kpn-table">
      <thead>
      <tr>
        <th>Key</th> <!-- Sleutel -->
        <th>Value</th> <!-- Waarde -->
      </tr>
      </thead>
      <tbody>
      <tr *ngFor="let tag of tags.tags">
        <td>{{tag.key}}</td>
        <td>{{tag.value}}</td>
      </tr>
      </tbody>
    </table>
  `
})
export class TagsComponent {
  @Input() tags: Tags;
}
