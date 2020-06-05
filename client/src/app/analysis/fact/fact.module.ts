import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDialogModule} from "@angular/material/dialog";
import {MatDividerModule} from "@angular/material/divider";
import {MatIconModule} from "@angular/material/icon";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatRadioModule} from "@angular/material/radio";
import {MatSortModule} from "@angular/material/sort";
import {MatTableModule} from "@angular/material/table";
import {RouterModule} from "@angular/router";
import {MarkdownModule} from "ngx-markdown";
import {SharedModule} from "../../components/shared/shared.module";
import {FactAddedComponent} from "./descriptions/fact-added.component";
import {FactBecomeOrphanComponent} from "./descriptions/fact-become-orphan.component";
import {FactDeletedComponent} from "./descriptions/fact-deleted.component";
import {FactIntegrityCheckFailedComponent} from "./descriptions/fact-integrity-check-failed.component";
import {FactIntegrityCheckComponent} from "./descriptions/fact-integrity-check.component";
import {FactLostBicycleNodeTagComponent} from "./descriptions/fact-lost-bicycle-node-tag.component";
import {FactLostCanoeNodeTagComponent} from "./descriptions/fact-lost-canoe-node-tag.component";
import {FactLostHikingNodeTagComponent} from "./descriptions/fact-lost-hiking-node-tag.component";
import {FactLostHorseNodeTagComponent} from "./descriptions/fact-lost-horse-node-tag.component";
import {FactLostInlineSkateNodeTagComponent} from "./descriptions/fact-lost-inline-skate-node-tag.component";
import {FactLostMotorboatNodeTagComponent} from "./descriptions/fact-lost-motorboat-node-tag.component";
import {FactLostRouteTagsComponent} from "./descriptions/fact-lost-route-tags.component";
import {FactNameMissingComponent} from "./descriptions/fact-name-missing.component";
import {FactNetworkExtraMemberNodeComponent} from "./descriptions/fact-network-extra-member-node.component";
import {FactNetworkExtraMemberRelationComponent} from "./descriptions/fact-network-extra-member-relation.component";
import {FactNetworkExtraMemberWayComponent} from "./descriptions/fact-network-extra-member-way.component";
import {FactNodeInvalidSurveyDateComponent} from "./descriptions/fact-node-invalid-survey-date.component";
import {FactNodeMemberMissingComponent} from "./descriptions/fact-node-member-missing.component";
import {FactOrphanNodeComponent} from "./descriptions/fact-orphan-node.component";
import {FactOrphanRouteComponent} from "./descriptions/fact-orphan-route.component";
import {FactRouteAnalysisFailedComponent} from "./descriptions/fact-route-analysis-failed.component";
import {FactRouteBrokenComponent} from "./descriptions/fact-route-broken.component";
import {FactRouteFixmetodoComponent} from "./descriptions/fact-route-fixmetodo.component";
import {FactRouteIncompleteOkComponent} from "./descriptions/fact-route-incomplete-ok.component";
import {FactRouteIncompleteComponent} from "./descriptions/fact-route-incomplete.component";
import {FactRouteInvalidSortingOrderComponent} from "./descriptions/fact-route-invalid-sorting-order.component";
import {FactRouteInvalidSurveyDateComponent} from "./descriptions/fact-route-invalid-survey-date.component";
import {FactRouteNameMissingComponent} from "./descriptions/fact-route-name-missing.component";
import {FactRouteNodeMissingInWaysComponent} from "./descriptions/fact-route-node-missing-in-ways.component";
import {FactRouteNodeNameMismatchComponent} from "./descriptions/fact-route-node-name-mismatch.component";
import {FactRouteNotBackwardComponent} from "./descriptions/fact-route-not-backward.component";
import {FactRouteNotContiniousComponent} from "./descriptions/fact-route-not-continious.component";
import {FactRouteNotForwardComponent} from "./descriptions/fact-route-not-forward.component";
import {FactRouteNotOneWayComponent} from "./descriptions/fact-route-not-one-way.component";
import {FactRouteOneWayComponent} from "./descriptions/fact-route-one-way.component";
import {FactRouteOverlappingWaysComponent} from "./descriptions/fact-route-overlapping-ways.component";
import {FactRouteRedundantNodesComponent} from "./descriptions/fact-route-redundant-nodes.component";
import {FactRouteSuspiciousWaysComponent} from "./descriptions/fact-route-suspicious-ways.component";
import {FactRouteTagInvalidComponent} from "./descriptions/fact-route-tag-invalid.component";
import {FactRouteTagMissingComponent} from "./descriptions/fact-route-tag-missing.component";
import {FactRouteUnaccessibleComponent} from "./descriptions/fact-route-unaccessible.component";
import {FactRouteUnexpectedNodeComponent} from "./descriptions/fact-route-unexpected-node.component";
import {FactRouteUnexpectedRelationComponent} from "./descriptions/fact-route-unexpected-relation.component";
import {FactRouteUnusedSegmentsComponent} from "./descriptions/fact-route-unused-segments.component";
import {FactRouteWithoutWaysComponent} from "./descriptions/fact-route-without-ways.component";
import {FactWasOrphanComponent} from "./descriptions/fact-was-orphan.component";
import {FactDescriptionComponent} from "./fact-description.component";
import {FactLevelComponent} from "./fact-level.component";
import {FactNameComponent} from "./fact-name.component";
import {FactsComponent} from "./facts.component";

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
    RouterModule
  ],
  declarations: [
    FactsComponent,
    FactLevelComponent,
    FactNameComponent,
    FactRouteNotContiniousComponent,
    FactRouteUnusedSegmentsComponent,
    FactRouteNodeMissingInWaysComponent,
    FactRouteRedundantNodesComponent,
    FactRouteWithoutWaysComponent,
    FactRouteFixmetodoComponent,
    FactRouteNameMissingComponent,
    FactRouteTagMissingComponent,
    FactRouteTagInvalidComponent,
    FactRouteUnexpectedNodeComponent,
    FactRouteUnexpectedRelationComponent,
    FactNetworkExtraMemberNodeComponent,
    FactNetworkExtraMemberWayComponent,
    FactNetworkExtraMemberRelationComponent,
    FactNodeMemberMissingComponent,
    FactIntegrityCheckFailedComponent,
    FactOrphanRouteComponent,
    FactOrphanNodeComponent,
    FactRouteIncompleteComponent,
    FactRouteIncompleteOkComponent,
    FactRouteUnaccessibleComponent,
    FactRouteInvalidSortingOrderComponent,
    FactRouteNodeNameMismatchComponent,
    FactRouteNotForwardComponent,
    FactRouteNotBackwardComponent,
    FactRouteAnalysisFailedComponent,
    FactRouteOverlappingWaysComponent,
    FactRouteSuspiciousWaysComponent,
    FactRouteBrokenComponent,
    FactRouteOneWayComponent,
    FactRouteNotOneWayComponent,
    FactNameMissingComponent,
    FactAddedComponent,
    FactBecomeOrphanComponent,
    FactDeletedComponent,
    FactIntegrityCheckComponent,
    FactLostBicycleNodeTagComponent,
    FactLostHikingNodeTagComponent,
    FactLostHorseNodeTagComponent,
    FactLostMotorboatNodeTagComponent,
    FactLostCanoeNodeTagComponent,
    FactLostRouteTagsComponent,
    FactLostInlineSkateNodeTagComponent,
    FactWasOrphanComponent,
    FactDescriptionComponent,
    FactNodeInvalidSurveyDateComponent,
    FactRouteInvalidSurveyDateComponent
  ],
  exports: [
    FactNameComponent,
    FactDescriptionComponent,
    FactsComponent,
    FactLevelComponent
  ]
})
export class FactModule {
}
