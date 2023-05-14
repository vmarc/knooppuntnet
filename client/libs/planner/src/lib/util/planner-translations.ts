import { Map as TranslationMap } from 'immutable';
import { ColourTranslator } from './colour-translator';

export class PlannerTranslations {
  private static readonly translations: TranslationMap<string, string> =
    this.buildTranslations();

  public static translate(key: string): string {
    return this.translations.get(key);
  }

  public static colour(colour: string): string {
    return new ColourTranslator(this.translations).translate(colour);
  }

  private static buildTranslations(): TranslationMap<string, string> {
    const keysAndValues: Array<[string, string]> = [];

    keysAndValues.push(['head', $localize`:@@plan.head:Head`]);

    keysAndValues.push([
      'follow-colour',
      $localize`:@@plan.follow-colour:Follow colour`,
    ]);

    keysAndValues.push([
      'heading-north',
      $localize`:@@plan.heading.north:north`,
    ]);
    keysAndValues.push([
      'heading-north-east',
      $localize`:@@plan.heading.north-east:north-east`,
    ]);
    keysAndValues.push(['heading-east', $localize`:@@plan.heading.east:east`]);
    keysAndValues.push([
      'heading-south-east',
      $localize`:@@plan.heading.south-east:south-east`,
    ]);
    keysAndValues.push([
      'heading-south',
      $localize`:@@plan.heading.south:south`,
    ]);
    keysAndValues.push([
      'heading-south-west',
      $localize`:@@plan.heading.south-west:south-west`,
    ]);
    keysAndValues.push(['heading-west', $localize`:@@plan.heading.west:west`]);
    keysAndValues.push([
      'heading-north-west',
      $localize`:@@plan.heading.north-west:north-west`,
    ]);

    keysAndValues.push([
      'command-continue',
      $localize`:@@plan.command.continue:Continue`,
    ]);
    keysAndValues.push([
      'command-turn-slight-left',
      $localize`:@@plan.command.turn-slight-left:Slight left`,
    ]);
    keysAndValues.push([
      'command-turn-slight-right',
      $localize`:@@plan.command.turn-slight-right:Slight right`,
    ]);
    keysAndValues.push([
      'command-turn-left',
      $localize`:@@plan.command.turn-left:Turn left`,
    ]);
    keysAndValues.push([
      'command-turn-right',
      $localize`:@@plan.command.turn-right:Turn right`,
    ]);
    keysAndValues.push([
      'command-turn-sharp-left',
      $localize`:@@plan.command.turn-sharp-left:Sharp left`,
    ]);
    keysAndValues.push([
      'command-turn-sharp-right',
      $localize`:@@plan.command.turn-sharp-right:Sharp right`,
    ]);

    keysAndValues.push(['black', $localize`:@@route.colour.black:black`]);
    keysAndValues.push(['blue', $localize`:@@route.colour.blue:blue`]);
    keysAndValues.push(['brown', $localize`:@@route.colour.brown:brown`]);
    keysAndValues.push(['gray', $localize`:@@route.colour.gray:gray`]);
    keysAndValues.push(['green', $localize`:@@route.colour.green:green`]);
    keysAndValues.push(['grey', $localize`:@@route.colour.grey:grey`]);
    keysAndValues.push(['orange', $localize`:@@route.colour.orange:orange`]);
    keysAndValues.push(['purple', $localize`:@@route.colour.purple:purple`]);
    keysAndValues.push(['red', $localize`:@@route.colour.red:red`]);
    keysAndValues.push(['violet', $localize`:@@route.colour.violet:violet`]);
    keysAndValues.push(['white', $localize`:@@route.colour.white:white`]);
    keysAndValues.push(['yellow', $localize`:@@route.colour.yellow:yellow`]);

    keysAndValues.push(['or', $localize`:@@route.colour.or:or`]);

    return TranslationMap<string, string>(keysAndValues);
  }
}
