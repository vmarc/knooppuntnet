import {ValidationErrors} from "@angular/forms";

export class Util {

  public static json(object: any): string {
    return JSON.stringify(object);
  }

  public static toMessages(errors: ValidationErrors | null): string[] {
    const messages: Array<string> = [];
    for (const key in errors) {
      messages.push(key);
    }
    return messages;
  }

}
