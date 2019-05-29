import {Component, Input} from "@angular/core";
import {TagDiffs} from "../../../kpn/shared/diff/tag-diffs";

@Component({
  selector: "kpn-tag-diffs",
  template: `
    <div *ngIf="!!tagDiffs">
      <div class="title"></div>

      <table class="kpn-table" title="Tag differences">
        <thead>
          <tr>
            <th>
            </th>
            <th>Key</th> <!-- Sleutel -->
            <th>Before</th> <!-- Voor -->
            <th>After</th> <!-- Na -->
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let tagDetail of tagDiffs.mainTags">
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
  
          <tr *ngIf="hasSeparator()">
            <td colspan="4"></td>
          </tr>
  
          <tr *ngFor="let tagDetail of tagDiffs.extraTags">
            <td></td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
  styles: [`
    .title {
      margin-top: 2px;
      margin-bottom: 4px;
    }
  `]
})
export class TagDiffsComponent {

  @Input() tagDiffs: TagDiffs;

  hasSeparator(): boolean {
    return !this.tagDiffs.mainTags.isEmpty() && !this.tagDiffs.extraTags.isEmpty();
  }

}
