import {Component, OnInit} from '@angular/core';
import {TranslationsService} from "./translations.service";

@Component({
  selector: 'translations',
  templateUrl: './translations.component.html',
  styleUrls: ['./translations.component.scss']
})
export class TranslationsComponent implements OnInit {

  content = "Translations";

  constructor(private translationsService: TranslationsService) {
  }

  ngOnInit() {
    this.translationsService.translationUnits().subscribe(content => {
      this.content = JSON.stringify(content, null, 2);
    });
  }

}
