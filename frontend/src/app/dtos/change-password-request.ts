export class ChangePasswordRequest {
  constructor(
    public id: number,
    public password: string
  ) {}
}
