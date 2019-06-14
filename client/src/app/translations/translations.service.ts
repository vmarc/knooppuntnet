import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {TranslationFile} from "./domain/translation-file";
import {XliffParser} from "./domain/xliff-parser";

@Injectable()
export class TranslationsService {

  constructor(private http: HttpClient) {
  }

  public loadTranslationFile(language: string): Observable<TranslationFile> {
    const url = `https://raw.githubusercontent.com/vmarc/knooppuntnet/master/client/src/locale/translations.${language}.xlf`;
    return this.http.get(url, {responseType: "text"}).pipe(
      map(response => {
        return new XliffParser().parse(response);
      })
    );
  }

}
