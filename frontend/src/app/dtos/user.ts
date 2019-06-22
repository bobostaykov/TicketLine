import {UserType} from '../datatype/user_type';
import {News} from './news';

export class User {
  constructor(
    public id: number,
    public username: string,
    public password: string,
    public type: UserType,
    public userSince: string,
    public lastLogin: string,
    public readNews: News[]
  ) {}
}
