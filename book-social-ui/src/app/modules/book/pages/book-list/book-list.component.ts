import { Component, OnInit } from '@angular/core';
import { PageResponseBookResponseDto } from '../../../../services/models/page-response-book-response-dto';
import { BookService } from '../../../../services/services/book.service';
import { Router } from '@angular/router';
import { BookResponseDto } from '../../../../services/models';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.css'
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponseDto = {};
  page = 0;
  size = 4;
  pages: any = [];
  message = '';
  level: 'success' | 'error' = 'success';

  constructor(
    private bookService: BookService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (booksPage) => {
        this.bookResponse = booksPage;
        this.pages = Array(this.bookResponse.totalPages)
          .fill(0)
          .map((x, i) => i);
      }
    });
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  get isLastPage() {
    return this.page === this.bookResponse.totalPages as number - 1;
  }

  borrowBook(book: BookResponseDto) {
    this.message = '';
    this.level = 'success';
    this.bookService.borrowBook({
      id: book.id as number
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Book successfully added to your list';
      },
      error: (err) => {
        console.log(err);
        this.level = 'error';
        this.message = this.extractRelevantMessage(err.error.businessErrorDescription);
      }
    });
  }

  displayBookDetails(book: BookResponseDto) {
    this.router.navigate(['books', 'details', book.id]);
  }

  private extractRelevantMessage(message: string): string {
    const relevantPart = 'The requested book';
    const startIndex = message.indexOf(relevantPart);
    return startIndex !== -1 ? message.substring(startIndex) : message;
  }
}
