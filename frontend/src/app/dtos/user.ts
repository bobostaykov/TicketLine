export class User {
  constructor(
    public id: number,
    public name: string,
    public type: userType,
    public userSince: string,
    public lastLogin: string
  ) {}
}

enum userType {
  'ADMIN',
  'SELLER'
}
