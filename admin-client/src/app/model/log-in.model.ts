export class LogInModel {
  constructor(
    private email: string,
    private accessToken: string,
    private role: string
  ) {
  }

  getRole(): string {
    return this.role;
  }
}
