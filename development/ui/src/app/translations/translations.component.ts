import {Component, OnInit} from '@angular/core';
import {TranslationsService} from "./translations.service";
import {TranslationUnit} from "./domain/translation-unit";
import {XliffWriter} from "./domain/xliff-writer";
import {TranslationFile} from "./domain/translation-file";
import {saveAs} from "file-saver";

@Component({
  selector: 'translations',
  templateUrl: './translations.component.html',
  styleUrls: ['./translations.component.scss']
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
