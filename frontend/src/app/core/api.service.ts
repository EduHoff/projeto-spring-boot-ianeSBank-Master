import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({providedIn:'root'})
export class ApiService {
  private readonly root='http://localhost:8080/api';
    constructor(private http:HttpClient){}
    get<T=any>(path:string):Observable<T>{return this.http.get<T>(this.root+path);}
    post<T=any>(path:string,data:any):Observable<T>{return this.http.post<T>(this.root+path,data);}
    put<T=any>(path:string,data:any):Observable<T>{return this.http.put<T>(this.root+path,data);}
    patch<T=any>(path:string,data:any):Observable<T>{return this.http.patch<T>(this.root+path,data);}
    delete<T=any>(path:string):Observable<T>{return this.http.delete<T>(this.root+path);}
}
