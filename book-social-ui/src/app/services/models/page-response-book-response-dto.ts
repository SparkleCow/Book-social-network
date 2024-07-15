/* tslint:disable */
/* eslint-disable */
import { BookResponseDto } from '../models/book-response-dto';
export interface PageResponseBookResponseDto {
  content?: Array<BookResponseDto>;
  first?: boolean;
  last?: boolean;
  page?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
