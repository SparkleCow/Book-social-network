import { Component, OnInit } from '@angular/core';
import { BorrowedBookResponse, PageResponseBorrowedBookResponse } from '../../../../services/models';
import { BookService } from '../../../../services/services';

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrl: './returned-books.component.css'
})
export class ReturnedBooksComponent implements OnInit {

  books: PageResponseBorrowedBookResponse = {};
  page: number = 0;
  size: number = 5
  pages: number[] = [];

  constructor(private _bookService:BookService){}

  ngOnInit(): void {
    this.findReturnedBooks();
  }

  findReturnedBooks(){
    this._bookService.findBooksReturned({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (res: PageResponseBorrowedBookResponse) => {
        this.books = res;
        this.pages = Array(res.totalPages).fill(0).map((x, i) => i);
      }
    })
  }

  alertApprovedBook(){
    alert("This book has been already returned")
  }

  approveBookReturned(id: number | undefined) {
    if(!id){
      return;
    }
    this._bookService.approveReturnBorrowedBook({id: id}).subscribe({
      next: ()=>{
        this.findReturnedBooks();
      }
    })
  }

  get isLastPage() {
    return this.page === this.books.totalPages as number - 1;
  }

  gotToPage(page: number) {
    this.page = page;
    this.findReturnedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findReturnedBooks();
  }

  goToLastPage() {
    this.page = this.books.totalPages as number - 1;
    this.findReturnedBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findReturnedBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findReturnedBooks();
  }
}
