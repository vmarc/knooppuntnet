import {List} from "immutable";
import {Map as TranslationMap} from "immutable";

export class ColourTranslator {

  constructor(private readonly translations: TranslationMap<string, string>) {
  }

  translate(colour: string): string {
    const splitted = List<string>(colour.split(";"));
    const translatedColours = splitted.map(colourKey => {
      const colourKeyElements = List<string>(colourKey.split("-"));
      const translatedColourElements = colourKeyElements.map(colourKeyElement => this.translateColour(colourKeyElement));
      return translatedColourElements.join("-");
    });
    return translatedColours.join("/");
  }

  private translateColour(key: string): string {
    if (this.translations) {
      const translation =  this.translations.get(key);
      if (translation) {
        return translation;
      }
      return key;
    }
    return "";
  }

}

