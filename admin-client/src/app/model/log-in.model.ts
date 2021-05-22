import { UserRole } from "./log-in";

export class LogInModel {
  constructor(
    private email: string,
    private accessToken: string,
    private id: number,
    private role: string
  ) {
  }

  getRole(): UserRole {
    return this.role === 'SUPER_ADMIN' ? UserRole.SUPER_ADMIN : UserRole.UNAUTHORIZED;
  }
}
