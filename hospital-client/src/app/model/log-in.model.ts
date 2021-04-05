export class LogInModel {
  constructor(
    private email: string,
    private accessToken: string,
    private role: string
  ) {
  }

  getRole(): UserRole {
    switch(this.role){
      case 'ROLE_ADMIN': return UserRole.ADMIN;
      case 'ROLE_DOCTOR': return UserRole.DOCTOR;
      default : return UserRole.UNAUTHORIZED;
    }
  }
}

export enum UserRole{
  ADMIN,
  DOCTOR,
  UNAUTHORIZED
}