import {Component, OnInit} from '@angular/core';
import {saveAs} from "file-saver";
import {TranslationFile} from "./domain/translation-file";
import {TranslationUnit} from "./domain/translation-unit";
import {XliffWriter} from "./domain/xliff-writer";
import {TranslationsService} from "./translations.service";

@Component({
  selector: 'translations',
  template: `
    <h1>Translations</h1>

    Number of translations: {{translationUnits.length}}

    <mat-divider></mat-divider>
    <button mat-raised-button color="primary" (click)="export()">Export</button>
    <mat-divider></mat-divider>

    <translation-table [translationUnits]="translationFile?.translationUnits">
    </translation-table>
  `,
  styles: [`
    mat-table {
      width: 100%;
    }
  `]
})
export class TranslationsComponent implements OnInit {

  translationFile: TranslationFile;
  translationUnits: Array<TranslationUnit> = [];

  constructor(private translationsService: TranslationsService) {
  }

  ngOnInit() {
    this.translationsService.translationUnits().subscribe(translationFile => {
      this.translationFile = translationFile;
      this.translationUnits = translationFile.translationUnits;
    });
  }

  export() {
    console.log("Export");
    const content = new XliffWriter().write(this.translationFile);
    const blob = new Blob([content], {type: 'application/xml'});
    let filename = "messages." + this.translationFile.targetLanguage + ".xlf";
    saveAs(blob, filename);
  }
}
