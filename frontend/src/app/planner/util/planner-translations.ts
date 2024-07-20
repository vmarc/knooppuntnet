import { ColourTranslator } from './colour-translator';

export class PlannerTranslations {
  private static readonly translations = new Map<string, string>([
    ['black', $localize`:@@route.colour.black:black`],
    ['blue', $localize`:@@route.colour.blue:blue`],
    ['brown', $localize`:@@route.colour.brown:brown`],
    ['gray', $localize`:@@route.colour.gray:gray`],
    ['green', $localize`:@@route.colour.green:green`],
    ['grey', $localize`:@@route.colour.grey:grey`],
    ['orange', $localize`:@@route.colour.orange:orange`],
    ['purple', $localize`:@@route.colour.purple:purple`],
    ['red', $localize`:@@route.colour.red:red`],
    ['violet', $localize`:@@route.colour.violet:violet`],
    ['white', $localize`:@@route.colour.white:white`],
    ['yellow', $localize`:@@route.colour.yellow:yellow`],
    //
    ['or', $localize`:@@route.colour.or:or`],
  ]);

  public static colour(colour: string): string {
    return new ColourTranslator(this.translations).translate(colour);
  }
}
