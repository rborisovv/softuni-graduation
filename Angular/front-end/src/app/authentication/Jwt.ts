export class Jwt {
  static jwtToken: string = 'JWT-TOKEN';

  static obtainJwtHeader(): string {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${this.jwtToken}=`);
    return 'Bearer ' + parts.pop().split(';').shift();
  }
}
