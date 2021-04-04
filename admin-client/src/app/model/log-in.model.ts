import { UserRole } from "./log-in";

export class LogInModel {
  constructor(
    private email: string,
    private accessToken: string,
    private role: string
  ) {
  }

  getRole(): UserRole {
    return this.role === "ROLE_SUPER_ADMIN" ? UserRole.ROLE_SUPER_ADMIN : UserRole.UNAUTHORIZED;
  }
}
