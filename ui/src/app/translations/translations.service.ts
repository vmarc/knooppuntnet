import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Observable} from "rxjs";
import {XliffParser} from "./domain/xliff-parser";
import {TranslationFile} from "./domain/translation-file";

@Injectable()
export class TranslationsService {

  constructor(private http: HttpClient) {
  }

  public translationUnits(): Observable<TranslationFile> {
    const url = "https://raw.githubusercontent.com/vmarc/knooppuntnet/develop/ui/src/locale/messages.fr.xlf";
    return this.http.get(url, {responseType: 'text'}).pipe(
      map(response => {
        return new XliffParser().parse(response);
      })
    );
  }

}
