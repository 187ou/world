export interface SetPasswordDto {
  email: string;
  code: string;
  newPassword: string;
  confirmPassword: string;
}
