import { RoleType } from '../enums/role-enum'

export interface RegisterData {
  username: string;
  firstName: string;
  lastName: string;
  password: string;
  role: RoleType;
}