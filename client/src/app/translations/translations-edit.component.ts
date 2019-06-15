import {Component, Input, OnInit} from "@angular/core";
import {saveAs} from "file-saver";
import {TranslationFile} from "./domain/translation-file";
import {XliffWriter} from "./domain/xliff-writer";
import {TranslationUnit} from "./domain/translation-unit";
import {List} from "immutable";

@Component({
  selector: "kpn-translations-edit",
  template: `

    Number of translations: {{translationCount()}},
    to be translated: {{toBeTranslatedCount()}},
    not saved yet:  {{dirtyCount()}}

    <button mat-stroked-button (click)="save()">Save to file</button>

    <mat-divider></mat-divider>

    <kpn-translation-unit [translationUnit]="translationUnit" (edited)="edited($event)"></kpn-translation-unit>

    <kpn-translation-table
      [translationUnits]="translationUnits"
      (selected)="selectedTranslactionUnitChanged($event)">
    </kpn-translation-table>
  `,
  styles: [`

  `]
})
export class TranslationsEditComponent implements OnInit {

  @Input() translationFile: TranslationFile;

  translationUnits: List<TranslationUnit> = List();
  translationUnit: TranslationUnit;

  ngOnInit(): void {
    this.translationUnits = this.translationFile.translationUnits;
  }

  save() {
    const newTranslationFile = new TranslationFile(
      this.translationFile.sourceLanguage,
      this.translationFile.targetLanguage,
      this.translationUnits
    );
    const content = new XliffWriter().write(newTranslationFile);
    const blob = new Blob([content], {type: "application/xml"});
    const filename = "messages." + this.translationFile.targetLanguage + ".xlf";
    saveAs(blob, filename);
  }

  translationCount(): number {
    return this.translationUnits.size;
  }

  toBeTranslatedCount(): number {
    return this.translationUnits.filter(tu => tu.state === "new").size;
  }

  dirtyCount(): number {
    return this.translationUnits.filter(tu => tu.dirty === true).size;
  }

  selectedTranslactionUnitChanged(translationUnit: TranslationUnit): void {
    this.translationUnit = translationUnit;
  }

  edited(translationUnit: TranslationUnit): void {
    this.translationUnits = this.translationUnits.map(tu => tu.id === translationUnit.id ? translationUnit : tu);
  }

}
