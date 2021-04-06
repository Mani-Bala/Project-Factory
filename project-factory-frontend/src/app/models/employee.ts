export class Employee {
  id: number;
  emailId: string;
  firstName: string;
  lastName: string;
  isActive: boolean;
  createdBy: number;
  createdOn: any;
  encryptedLoginToken: string;
  tokenExpTime: number;
  loginToken: string;
  displayFullName: string;
  passwordResetRequest: boolean;
}
