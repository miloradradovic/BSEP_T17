export class LogInModel {
  constructor(
    private email: string,
    private accessToken: string,
    private id: number,
    private role: string,
  ) {
  }

  getRole(): UserRole {
    switch(this.role){
      case 'ADMIN': return UserRole.ADMIN;
      case 'DOCTOR': return UserRole.DOCTOR;
      default : return UserRole.UNAUTHORIZED;
    }
  }
}

export enum UserRole{
  ADMIN,
  DOCTOR,
  UNAUTHORIZED
}
