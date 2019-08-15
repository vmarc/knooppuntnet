import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatCheckboxModule, MatDialogModule, MatDividerModule, MatIconModule, MatPaginatorModule, MatRadioModule, MatSortModule, MatTableModule} from "@angular/material";
import {RouterModule} from "@angular/router";
import {MarkdownModule} from "ngx-markdown";
import {SharedModule} from "../../components/shared/shared.module";
import {FactAddedComponent} from "./descriptions/fact-added.component";
import {FactBecomeIgnoredComponent} from "./descriptions/fact-become-ignored.component";
import {FactBecomeOrphanComponent} from "./descriptions/fact-become-orphan.component";
import {FactDeletedComponent} from "./descriptions/fact-deleted.component";
import {FactIgnoreForeignCountryComponent} from "./descriptions/fact-ignore-foreign-country.component";
import {FactIgnoreNetworkCollectionComponent} from "./descriptions/fact-ignore-network-collection.component";
import {FactIgnoreNoNetworkNodesComponent} from "./descriptions/fact-ignore-no-network-nodes.component";
import {FactIgnoreUnsupportedSubsetComponent} from "./descriptions/fact-ignore-unsupported-subset.component";
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
import {FactNodeMemberMissingComponent} from "./descriptions/fact-node-member-missing.component";
import {FactOrphanNodeComponent} from "./descriptions/fact-orphan-node.component";
import {FactOrphanRouteComponent} from "./descriptions/fact-orphan-route.component";
import {FactRouteAnalysisFailedComponent} from "./descriptions/fact-route-analysis-failed.component";
import {FactRouteBrokenComponent} from "./descriptions/fact-route-broken.component";
import {FactRouteEndNodeMismatchComponent} from "./descriptions/fact-route-end-node-mismatch.component";
import {FactRouteFixmetodoComponent} from "./descriptions/fact-route-fixmetodo.component";
import {FactRouteIncompleteOkComponent} from "./descriptions/fact-route-incomplete-ok.component";
import {FactRouteIncompleteComponent} from "./descriptions/fact-route-incomplete.component";
import {FactRouteInvalidSortingOrderComponent} from "./descriptions/fact-route-invalid-sorting-order.component";
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
import {FactRouteReversedComponent} from "./descriptions/fact-route-reversed.component";
import {FactRouteStartNodeMismatchComponent} from "./descriptions/fact-route-start-node-mismatch.component";
import {FactRouteSuspiciousWaysComponent} from "./descriptions/fact-route-suspicious-ways.component";
import {FactRouteTagInvalidComponent} from "./descriptions/fact-route-tag-invalid.component";
import {FactRouteTagMissingComponent} from "./descriptions/fact-route-tag-missing.component";
import {FactRouteUnaccessibleComponent} from "./descriptions/fact-route-unaccessible.component";
import {FactRouteUnexpectedNodeComponent} from "./descriptions/fact-route-unexpected-node.component";
import {FactRouteUnexpectedRelationComponent} from "./descriptions/fact-route-unexpected-relation.component";
import {FactRouteUnusedSegmentsComponent} from "./descriptions/fact-route-unused-segments.component";
import {FactRouteWithoutWaysComponent} from "./descriptions/fact-route-without-ways.component";
import {FactWasIgnoredComponent} from "./descriptions/fact-was-ignored.component";
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
    FactRouteEndNodeMismatchComponent,
    FactRouteStartNodeMismatchComponent,
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
    FactRouteReversedComponent,
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
    FactIgnoreForeignCountryComponent,
    FactIgnoreNoNetworkNodesComponent,
    FactIgnoreUnsupportedSubsetComponent,
    FactAddedComponent,
    FactBecomeIgnoredComponent,
    FactBecomeOrphanComponent,
    FactDeletedComponent,
    FactIgnoreNetworkCollectionComponent,
    FactIntegrityCheckComponent,
    FactLostBicycleNodeTagComponent,
    FactLostHikingNodeTagComponent,
    FactLostHorseNodeTagComponent,
    FactLostMotorboatNodeTagComponent,
    FactLostCanoeNodeTagComponent,
    FactLostRouteTagsComponent,
    FactLostInlineSkateNodeTagComponent,
    FactWasIgnoredComponent,
    FactWasOrphanComponent,
    FactDescriptionComponent
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
