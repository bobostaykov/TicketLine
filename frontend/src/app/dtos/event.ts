import {Show} from './show';
import {Artist} from './artist';
import {EventType} from '../datatype/event_type';

export class Event {
  constructor(
    public id: number,
    public name: string,
    public type: EventType,
    public durationInMinutes: number,
    public description: string,
    public content: string,
    public participatingArtists: Artist[],
    public shows: Show[]
  ) {}
}
