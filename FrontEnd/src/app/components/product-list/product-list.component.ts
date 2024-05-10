import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../classes/product";
import {CommonModule, CurrencyPipe, NgForOf} from "@angular/common";
import {Category} from "../../classes/category";
import {FormsModule} from "@angular/forms";
import {animate, style, transition, trigger} from "@angular/animations";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    NgForOf,
    CurrencyPipe,
    FormsModule,
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css',
  providers: [ProductService],
})
export class ProductListComponent implements OnInit{
  constructor(private productService: ProductService) { }

  products: Product[] = [];
  categories: Category[] = [];
  currentPage = 1;
  recordsPerPage = 10;
  selectedCategory: string = "";
  query: string = "";


  ngOnInit(): void {
    this.listProducts();
    this.listCategories();
  }


  private listProducts() {
    this.productService.getProducts().subscribe(
      data => {
        this.products = data;
      }
    )
  }

  private listCategories() {
    this.productService.getCategories().subscribe(
      data => {
        this.categories = data;
      }
    )
  }
  onCategoryClick(categoryName: string) {

      this.selectedCategory = categoryName;
      this.productService.getProductsByCategory(categoryName).subscribe(products => {
        this.products = products;
      });
  }
  deselectAll() {
    this.selectedCategory = "";
    this.listProducts()
  }

  protected readonly Math = Math;
    restCurrentPage() {
      this.currentPage--
    }
    addCurrentPage(){
      this.currentPage++
    }
  searchQuery(): void {
    this.productService.searchProducts(this.query).subscribe(
      data => {
        this.products = data;
      }
    )
  }

}
