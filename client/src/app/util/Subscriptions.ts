import {SubscriptionLike} from 'rxjs';

export class Subscriptions {

  protected subscriptions: SubscriptionLike[] = [];

  add(...subscriptions: SubscriptionLike[]) {
    this.subscriptions = this.subscriptions.concat(subscriptions);
  }

  unsubscribe() {
    this.subscriptions.forEach(sub => sub && sub.unsubscribe());
    this.subscriptions = [];
  }
}
