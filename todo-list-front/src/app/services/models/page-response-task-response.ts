/* tslint:disable */
/* eslint-disable */
import { TaskResponse } from '../models/task-response';
export interface PageResponseTaskResponse {
  content?: Array<TaskResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
