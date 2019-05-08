import {Hall} from './hall';
import {Event} from './event';

export class Show {
  constructor(
    public id: number,
    public event: Event,
    public dateTime: string,
    public hall: Hall,
    public description: string,
    public ticketsSold: number
  ) {}
}
