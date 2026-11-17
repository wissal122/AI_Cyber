import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getHello(): Observable<string> {
    return this.http.get(`${this.apiUrl}/hello`, {
      responseType: 'text'
    });
  }

  sendMessage(message: string): Observable<string> {
    return new Observable<string>((observer) => {

      fetch(`${this.apiUrl}/chat`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          message: message
        })
      })
      .then(async (response) => {

        if (!response.ok) {
          throw new Error(`Erreur HTTP ${response.status}`);
        }

        if (!response.body) {
          throw new Error('Le serveur ne retourne pas de flux.');
        }

        const reader = response.body.getReader();
        const decoder = new TextDecoder();

        while (true) {

          const { value, done } = await reader.read();

          if (done) {
            break;
          }

          const chunk = decoder.decode(value, {
            stream: true
          });

          observer.next(chunk);
        }

        observer.complete();
      })
      .catch((error) => {
        observer.error(error);
      });
    });
  }
}