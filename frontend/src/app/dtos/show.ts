import {Hall} from './hall';
import {Event} from './event';

export class Show {
  constructor(
    public id: number,
    public event: Event,
    public time: string,
    public date: string,
    public hall: Hall,
    public description: string,
    public ticketsSold: number
  ) {}
}
