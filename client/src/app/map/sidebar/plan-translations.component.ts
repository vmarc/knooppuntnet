import {ChangeDetectionStrategy} from "@angular/core";
import {AfterViewInit, ChangeDetectorRef, Component, ElementRef} from "@angular/core";
import {PlannerService} from "../planner.service";

@Component({
  selector: "kpn-plan-translations",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!isRegistryUpdated()">

      <span id="head" i18n="@@plan.head">Head</span>
      <span id="onto" i18n="@@plan.onto">onto</span>

      <span id="heading-north" i18n="@@plan.heading.north">north</span>
      <span id="heading-north-east" i18n="@@plan.heading.north-east">north-east</span>
      <span id="heading-east" i18n="@@plan.heading.east">east</span>
      <span id="heading-south-east" i18n="@@plan.heading.south-east">south-east</span>
      <span id="heading-south" i18n="@@plan.heading.south">south</span>
      <span id="heading-south-west" i18n="@@plan.heading.south-west">south-west</span>
      <span id="heading-west" i18n="@@plan.heading.west">west</span>
      <span id="heading-north-west" i18n="@@plan.heading.north-west">north-west</span>

      <span id="command-continue" i18n="@@plan.command.continue">Continue</span>
      <span id="command-continue-street" i18n="@@plan.command.continue.street">Continue on</span>
      <span id="command-turn-slight-left" i18n="@@plan.command.turn-slight-left">Slight left</span>
      <span id="command-turn-slight-left-street" i18n="@@plan.command.turn-slight-left.street">Slight left onto</span>
      <span id="command-turn-slight-right" i18n="@@plan.command.turn-slight-right">Slight right</span>
      <span id="command-turn-slight-right-street" i18n="@@plan.command.turn-slight-right.street">Slight right onto</span>
      <span id="command-turn-left" i18n="@@plan.command.turn-left">Turn left</span>
      <span id="command-turn-left-street" i18n="@@plan.command.turn-left.street">Turn left onto</span>
      <span id="command-turn-right" i18n="@@plan.command.turn-right">Turn right</span>
      <span id="command-turn-right-street" i18n="@@plan.command.turn-right.street">Turn right onto</span>
      <span id="command-turn-sharp-left" i18n="@@plan.command.turn-sharp-left">Sharp left</span>
      <span id="command-turn-sharp-left-street" i18n="@@plan.command.turn-sharp-left.street">Sharp left onto</span>
      <span id="command-turn-sharp-right" i18n="@@plan.command.turn-sharp-right">Sharp right</span>
      <span id="command-turn-sharp-right-street" i18n="@@plan.command.turn-sharp-right.street">Sharp right onto</span>
    </div>
  `,
  styles: [`
    :host {
      display: none;
    }
  `]
})
export class PlanTranslationsComponent implements AfterViewInit {

  constructor(private element: ElementRef,
              private plannerService: PlannerService,
              private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit(): void {
    const divElement = this.element.nativeElement.children[0];
    if (divElement != null) {
      const translationElements = divElement.children;
      this.plannerService.updateTranslationRegistry(translationElements);
      this.cdr.detectChanges();
    }
  }

  isRegistryUpdated(): boolean {
    return this.plannerService.isTranslationRegistryUpdated();
  }
}
