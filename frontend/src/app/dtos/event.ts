import {Show} from './show';
import {Artist} from './artist';

export class Event {
  constructor(
    public id: number,
    public name: string,
    public type: eventType,
    public durationInMinutes: number,
    public description: string,
    public content: string,
    public participatingArtists: Artist[],
    public shows: Show[]
  ) {}
}

export enum eventType {
  THEATRE,
  OPERA,
  FESTIVAL,
  CONCERT,
  MOVIE,
  MUSICAL,
  SPORT
}
