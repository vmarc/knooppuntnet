import {Component} from "@angular/core";
import {TranslationFile} from "./domain/translation-file";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-translations",
  template: `
    <h1>Translations</h1>
    <kpn-translations-load *ngIf="!loaded" (translationFile)="translationFileLoaded($event)"></kpn-translations-load>
    <kpn-translations-edit *ngIf="loaded" [translationFile]="translationFile"></kpn-translations-edit>
  `
})
export class TranslationsComponent {

  translationFile: TranslationFile;

  loaded = false;

  translationFileLoaded(translationFile: TranslationFile) {
    this.translationFile = translationFile;
    this.loaded = true;
  }

}
