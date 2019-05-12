import {UserType} from '../datatype/user_type';

export class User {
  constructor(
    public id: number,
    public name: string,
    public type: UserType,
    public userSince: string,
    public lastLogin: string
  ) {}
}
