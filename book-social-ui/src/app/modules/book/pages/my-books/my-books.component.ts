import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../../services/services/book.service';
import { BookResponseDto, PageResponseBookResponseDto } from '../../../../services/models';
import { Router } from '@angular/router';

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.css'
})
export class MyBooksComponent implements OnInit {
  bookResponse: PageResponseBookResponseDto = {};
  withoutBooksCreated: boolean = false
  page = 0;
  size = 4;
  pages: any = [];

  constructor(private bookService: BookService,private router: Router){}

  ngOnInit(): void{
    this.findAllBooks();
  }

  private findAllBooks(){
    this.bookService.findBooksByOwner({
      page: this.page,
      size: this.size
    })
      .subscribe({
        next: (books) => {
          this.bookResponse = books;
          this.pages = Array(this.bookResponse.totalPages)
            .fill(0)
            .map((x, i) => i);
          if(!books.content || books.content.length == 0){
            this.withoutBooksCreated = true;
          }
        }
      });
  }

  gotToPage(page: number){
    this.page = page;
    this.findAllBooks();
  }

  goToFirstPage(){
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage(){
    this.page --;
    this.findAllBooks();
  }

  goToLastPage(){
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  goToNextPage(){
    this.page++;
    this.findAllBooks();
  }

  get isLastPage(){
    return this.page === this.bookResponse.totalPages as number - 1;
  }

  archiveBook(book: BookResponseDto){
    this.bookService.updateArchivedStatus({
      id: book.id as number
    }).subscribe({
      next: () => {
        book.archived = !book.archived;
      }
    });
  }

  shareBook(book: BookResponseDto){
    this.bookService.updateShareableStatus({
      id: book.id as number
    }).subscribe({
      next: () => {
        book.shareable = !book.shareable;
      }
    });
  }

  editBook(book: BookResponseDto){
    this.router.navigate(['books', 'manage', book.id]);
  }
}
