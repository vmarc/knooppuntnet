import { ColourTranslator } from './colour-translator';

export class PlannerTranslations {
  private static readonly translations = new Map<string, string>([
    ['head', $localize`:@@plan.head:Head`],
    //
    ['follow-colour', $localize`:@@plan.follow-colour:Follow colour`],
    //
    ['heading-north', $localize`:@@plan.heading.north:north`],
    ['heading-north-east', $localize`:@@plan.heading.north-east:north-east`],
    ['heading-east', $localize`:@@plan.heading.east:east`],
    ['heading-south-east', $localize`:@@plan.heading.south-east:south-east`],
    ['heading-south', $localize`:@@plan.heading.south:south`],
    ['heading-south-west', $localize`:@@plan.heading.south-west:south-west`],
    ['heading-west', $localize`:@@plan.heading.west:west`],
    ['heading-north-west', $localize`:@@plan.heading.north-west:north-west`],
    //
    ['command-continue', $localize`:@@plan.command.continue:Continue`],
    ['command-turn-slight-left', $localize`:@@plan.command.turn-slight-left:Slight left`],
    ['command-turn-slight-right', $localize`:@@plan.command.turn-slight-right:Slight right`],
    ['command-turn-left', $localize`:@@plan.command.turn-left:Turn left`],
    ['command-turn-right', $localize`:@@plan.command.turn-right:Turn right`],
    ['command-turn-sharp-left', $localize`:@@plan.command.turn-sharp-left:Sharp left`],
    ['command-turn-sharp-right', $localize`:@@plan.command.turn-sharp-right:Sharp right`],
    //
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

  public static get(key: string): string {
    return this.translations.get(key);
  }

  public static colour(colour: string): string {
    return new ColourTranslator(this.translations).translate(colour);
  }
}
