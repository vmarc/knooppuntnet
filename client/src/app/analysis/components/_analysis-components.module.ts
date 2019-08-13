import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {
  MatCheckboxModule,
  MatDialogModule,
  MatDividerModule,
  MatIconModule,
  MatPaginatorModule,
  MatRadioModule,
  MatSlideToggleModule,
  MatSortModule,
  MatTableModule
} from "@angular/material";
import {MarkdownModule} from "ngx-markdown";
import {SharedModule} from "../../components/shared/shared.module";
import {ChangeHeaderComponent} from "./change-set/change-header.component";
import {ChangeSetInfoComponent} from "./change-set/change-set-info.component";
import {ChangesSetComponent} from "./change-set/change-set.component";
import {ChangesSetElementRefComponent} from "./change-set/components/change-set-element-ref.component";
import {ChangesSetElementRefsComponent} from "./change-set/components/change-set-element-refs.component";
import {ChangesSetNetworkComponent} from "./change-set/components/change-set-network.component";
import {ChangesSetOrphanNodesComponent} from "./change-set/components/change-set-orphan-nodes.component";
import {ChangesSetOrphanRoutesComponent} from "./change-set/components/change-set-orphan-routes.component";
import {ChangesTableComponent} from "./changes/changes-table.component";
import {ChangesComponent} from "./changes/changes.component";
import {FactCommaListComponent} from "./changes/fact-comma-list.component";
import {FactDiffsComponent} from "./changes/fact-diffs.component";
import {HistoryIncompleteWarningComponent} from "./changes/history-incomplete-warning.component";
import {TagDiffActionComponent} from "./changes/tag-diff-action.component";
import {TagDiffsTableComponent} from "./changes/tag-diffs-table.component";
import {TagDiffsTextComponent} from "./changes/tag-diffs-text.component";
import {TagDiffsComponent} from "./changes/tag-diffs.component";
import {FilterCheckboxGroupComponent} from "./filter/filter-checkbox-group.component";
import {FilterRadioGroupComponent} from "./filter/filter-radio-group.component";
import {FilterTitleComponent} from "./filter/filter-title.component";
import {FilterComponent} from "./filter/filter.component";

@NgModule({
  imports: [
    MarkdownModule,
    CommonModule,
    MatPaginatorModule,
    MatTableModule,
    MatDialogModule,
    MatSortModule,
    MatIconModule,
    MatDialogModule,
    MatDividerModule,
    SharedModule,
    MatRadioModule,
    MatCheckboxModule,
    MatSlideToggleModule
  ],
  declarations: [
    ChangeHeaderComponent,
    ChangeSetInfoComponent,
    ChangesTableComponent,
    ChangesSetComponent,
    ChangesSetNetworkComponent,
    ChangesSetOrphanNodesComponent,
    ChangesSetOrphanRoutesComponent,
    ChangesSetElementRefsComponent,
    ChangesSetElementRefComponent,
    HistoryIncompleteWarningComponent,
    TagDiffsComponent,
    TagDiffsTableComponent,
    TagDiffsTextComponent,
    TagDiffActionComponent,
    FactDiffsComponent,
    FactCommaListComponent,
    FilterComponent,
    FilterCheckboxGroupComponent,
    FilterRadioGroupComponent,
    FilterTitleComponent,
    ChangesComponent
  ],
  exports: [
    ChangeHeaderComponent,
    ChangeSetInfoComponent,
    ChangesTableComponent,
    ChangesSetComponent,
    ChangesSetNetworkComponent,
    ChangesSetOrphanNodesComponent,
    ChangesSetOrphanRoutesComponent,
    ChangesSetElementRefsComponent,
    ChangesSetElementRefComponent,
    HistoryIncompleteWarningComponent,
    TagDiffsComponent,
    TagDiffsTableComponent,
    TagDiffsTextComponent,
    TagDiffActionComponent,
    FactDiffsComponent,
    FactCommaListComponent,
    FilterComponent,
    FilterCheckboxGroupComponent,
    FilterRadioGroupComponent,
    FilterTitleComponent,
    ChangesComponent
  ]
})
export class AnalysisComponentsModule {
}
