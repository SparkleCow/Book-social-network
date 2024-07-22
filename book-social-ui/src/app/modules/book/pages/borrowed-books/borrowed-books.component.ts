import { Component, OnInit } from '@angular/core';
import { BookService, FeedbackService } from '../../../../services/services';
import { BookResponseDto, FeedbackRequestDto, PageResponseBorrowedBookResponse } from '../../../../services/models';

@Component({
  selector: 'app-borrowed-books',
  templateUrl: './borrowed-books.component.html',
  styleUrl: './borrowed-books.component.css'
})
export class BorrowedBooksComponent implements OnInit {

  books: PageResponseBorrowedBookResponse = {}
  selectedBook: BookResponseDto | undefined = undefined;
  feedbackRequest: FeedbackRequestDto = {bookId: 0, comment: '', note: 0};

  page: number = 0;
  size: number = 5;
  pages: number[] = [];

  constructor(private _bookService: BookService, private _feedbackService: FeedbackService) {}
  
  ngOnInit(): void {
    this.findBorrowedBooks();
  }

  findBorrowedBooks(){
    this._bookService.findBooksBorrowed({ page: this.page, size: this.size }).subscribe({
      next: (res: PageResponseBorrowedBookResponse) => {
        this.books = res;
        this.pages = Array(this.books.totalPages).fill(0).map((x, i) => i);        
      }
    });
  }

  get isLastPage() {
    return this.page === this.books.totalPages as number - 1;
  }

  gotToPage(page: number) {
    this.page = page;
    this.findBorrowedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findBorrowedBooks();
  }

  goToLastPage() {
    this.page = this.books.totalPages as number - 1;
    this.findBorrowedBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findBorrowedBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findBorrowedBooks();
  }
}
