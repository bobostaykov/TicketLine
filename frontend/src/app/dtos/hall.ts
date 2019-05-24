import {Show} from './show';
import {Location} from './location';
import {Seat} from './seat';
import {Sector} from './sector';

export class Hall {
  constructor(
    public id: number,
    public name: string,
    // public shows: Show[], TODO: do we need a list of shows here
    public location: Location,
    public seats: Seat[],
    public sector: Sector[]
  ) {}
}
