import {Component, OnInit} from '@angular/core';
import {TranslationsService} from "./translations.service";
import {TranslationUnit} from "./domain/translation-unit";

@Component({
  selector: 'translations',
  templateUrl: './translations.component.html',
  styleUrls: ['./translations.component.scss']
})
export class TranslationsComponent implements OnInit {

  translationUnits: TranslationUnit[] = [];

  constructor(private translationsService: TranslationsService) {
  }

  ngOnInit() {
    this.translationsService.translationUnits().subscribe(translationUnits => {
      this.translationUnits = translationUnits;
    });
  }

}
