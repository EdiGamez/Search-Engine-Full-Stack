import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {Product} from "../classes/product";
import {Category} from "../classes/category";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUrl = 'http://localhost:8080/products/v1';
  constructor(private httpClient: HttpClient) { }

  getProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>(`${this.baseUrl}/list`);
  }
  getCategories(): Observable<Category[]>{
    return this.httpClient.get<Category[]>(`${this.baseUrl}/categories`);
  }
  getProductsByCategory(categoryName: string): Observable<Product[]> {
    return this.httpClient.get<Product[]>(`${this.baseUrl}/list/category`, {
      params: {
        name: categoryName
      }
    });
  }
  searchProducts(query: string): Observable<Product[]> {
    return this.httpClient.get<Product[]>(`${this.baseUrl}/search`, {
      params: {
        query: query
      }
    }).pipe(
      catchError((error) => {
        if (error.status === 400) {

          return of([]);
        } else {

          throw error;
        }
      })
    );
  }
}
