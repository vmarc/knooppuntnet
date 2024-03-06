import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Fact } from '@api/custom';

@Component({
  selector: 'kpn-fact-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ factName }}`,
  standalone: true,
})
export class FactNameComponent implements OnInit {
  fact = input.required<Fact>();
  factName: string;

  ngOnInit(): void {
    if (this.fact() === 'Added') {
      this.factName = $localize`:@@fact.name.added:Added`;
    } else if (this.fact() === 'Deleted') {
      this.factName = $localize`:@@fact.name.deleted:Deleted`;
    } else if (this.fact() === 'IntegrityCheck') {
      this.factName = $localize`:@@fact.name.integrity-check:IntegrityCheck`;
    } else if (this.fact() === 'IntegrityCheckFailed') {
      this.factName = $localize`:@@fact.name.integrity-check-failed:IntegrityCheckFailed`;
    } else if (this.fact() === 'LostBicycleNodeTag') {
      this.factName = $localize`:@@fact.name.lost-bicycle-node-tag:LostBicycleNodeTag`;
    } else if (this.fact() === 'LostHikingNodeTag') {
      this.factName = $localize`:@@fact.name.lost-hiking-node-tag:LostHikingNodeTag`;
    } else if (this.fact() === 'LostRouteTags') {
      this.factName = $localize`:@@fact.name.lost-route-tags:LostRouteTags`;
    } else if (this.fact() === 'NameMissing') {
      this.factName = $localize`:@@fact.name.name-missing:NameMissing`;
    } else if (this.fact() === 'NetworkExtraMemberNode') {
      this.factName = $localize`:@@fact.name.network-extra-member-node:NetworkExtraMemberNode`;
    } else if (this.fact() === 'NetworkExtraMemberRelation') {
      this.factName = $localize`:@@fact.name.network-extra-member-relation:NetworkExtraMemberRelation`;
    } else if (this.fact() === 'NetworkExtraMemberWay') {
      this.factName = $localize`:@@fact.name.network-extra-member-way:NetworkExtraMemberWay`;
    } else if (this.fact() === 'NodeMemberMissing') {
      this.factName = $localize`:@@fact.name.node-member-missing:NodeMemberMissing`;
    } else if (this.fact() === 'OrphanNode') {
      this.factName = $localize`:@@fact.name.orphan-node:OrphanNode`;
    } else if (this.fact() === 'OrphanRoute') {
      this.factName = $localize`:@@fact.name.orphan-route:OrphanRoute`;
    } else if (this.fact() === 'RouteAnalysisFailed') {
      this.factName = $localize`:@@fact.name.route-analysis-failed:RouteAnalysisFailed`;
    } else if (this.fact() === 'RouteBroken') {
      this.factName = $localize`:@@fact.name.route-broken:RouteBroken`;
    } else if (this.fact() === 'RouteFixmetodo') {
      this.factName = $localize`:@@fact.name.route-fixmetodo:RouteFixmetodo`;
    } else if (this.fact() === 'RouteIncomplete') {
      this.factName = $localize`:@@fact.name.route-incomplete:RouteIncomplete`;
    } else if (this.fact() === 'RouteIncompleteOk') {
      this.factName = $localize`:@@fact.name.route-incomplete-ok:RouteIncompleteOk`;
    } else if (this.fact() === 'RouteNameMissing') {
      this.factName = $localize`:@@fact.name.route-name-missing:RouteNameMissing`;
    } else if (this.fact() === 'RouteNodeMissingInWays') {
      this.factName = $localize`:@@fact.name.route-node-missing-in-ways:RouteNodeMissingInWays`;
    } else if (this.fact() === 'RouteNodeNameMismatch') {
      this.factName = $localize`:@@fact.name.route-node-name-mismatch:RouteNodeNameMismatch`;
    } else if (this.fact() === 'RouteNotBackward') {
      this.factName = $localize`:@@fact.name.route-not-backward:RouteNotBackward`;
    } else if (this.fact() === 'RouteNotContinious') {
      this.factName = $localize`:@@fact.name.route-not-continious:RouteNotContinious`;
    } else if (this.fact() === 'RouteNotForward') {
      this.factName = $localize`:@@fact.name.route-not-forward:RouteNotForward`;
    } else if (this.fact() === 'RouteNotOneWay') {
      this.factName = $localize`:@@fact.name.route-not-one-way:RouteNotOneWay`;
    } else if (this.fact() === 'RouteOneWay') {
      this.factName = $localize`:@@fact.name.route-one-way:RouteOneWay`;
    } else if (this.fact() === 'RouteOverlappingWays') {
      this.factName = $localize`:@@fact.name.route-overlapping-ways:RouteOverlappingWays`;
    } else if (this.fact() === 'RouteRedundantNodes') {
      this.factName = $localize`:@@fact.name.route-redundant-nodes:RouteRedundantNodes`;
    } else if (this.fact() === 'RouteReversed') {
      this.factName = $localize`:@@fact.name.route-reversed:RouteReversed`;
    } else if (this.fact() === 'RouteSuspiciousWays') {
      this.factName = $localize`:@@fact.name.route-suspicious-ways:RouteSuspiciousWays`;
    } else if (this.fact() === 'RouteTagInvalid') {
      this.factName = $localize`:@@fact.name.route-tag-invalid:RouteTagInvalid`;
    } else if (this.fact() === 'RouteTagMissing') {
      this.factName = $localize`:@@fact.name.route-tag-missing:RouteTagMissing`;
    } else if (this.fact() === 'RouteInaccessible') {
      this.factName = $localize`:@@fact.name.route-inaccessible:RouteInaccessible`;
    } else if (this.fact() === 'RouteUnexpectedNode') {
      this.factName = $localize`:@@fact.name.route-unexpected-node:RouteUnexpectedNode`;
    } else if (this.fact() === 'RouteUnexpectedRelation') {
      this.factName = $localize`:@@fact.name.route-unexpected-relation:RouteUnexpectedRelation`;
    } else if (this.fact() === 'RouteUnusedSegments') {
      this.factName = $localize`:@@fact.name.route-unused-segments:RouteUnusedSegments`;
    } else if (this.fact() === 'RouteWithoutNodes') {
      this.factName = $localize`:@@fact.name.route-without-nodes:RouteWithoutNodes`;
    } else if (this.fact() === 'RouteWithoutWays') {
      this.factName = $localize`:@@fact.name.route-without-ways:RouteWithoutWays`;
    } else if (this.fact() === 'NodeInvalidSurveyDate') {
      this.factName = $localize`:@@fact.name.node-invalid-survey-date:NodeInvalidSurveyDate`;
    } else if (this.fact() === 'RouteInvalidSurveyDate') {
      this.factName = $localize`:@@fact.name.route-invalid-survey-date:RouteInvalidSurveyDate`;
    } else if (this.fact() === 'RouteNameDeprecatedNoteTag') {
      this.factName = $localize`:@@fact.name.route-name-deprecated-note-tag:RouteNameDeprecatedNoteTag`;
    } else {
      this.factName = this.fact() + '?';
    }
  }
}
