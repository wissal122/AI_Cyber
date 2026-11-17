import { Component, OnInit, OnDestroy, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from './api.service';
import { Subscription } from 'rxjs';

@Component({
  imports: [FormsModule],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App implements OnInit, OnDestroy {

  protected readonly title = signal('frontend');

  message = '';
  userMessage = '';
  isLoading = false;

  private chatSubscription?: Subscription;

  messages: { text: string; sender: 'user' | 'assistant' }[] = [
    {
      text: 'Bonjour 👋 Je suis votre assistant en cybersécurité. Comment puis-je vous aider ?',
      sender: 'assistant'
    }
  ];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.apiService.getHello().subscribe({
      next: (response) => {
        this.message = response;
      },
      error: (error) => {
        console.error('Erreur API:', error);
        this.message = 'Erreur de connexion au backend';
      }
    });
  }

  sendMessage(): void {

    if (!this.userMessage.trim() || this.isLoading) {
      return;
    }

    const question = this.userMessage;

    this.messages.push({
      text: question,
      sender: 'user'
    });

    this.userMessage = '';
    this.isLoading = true;

    // On crée immédiatement le message de l'assistant
    this.messages.push({
      text: '',
      sender: 'assistant'
    });

    // On récupère sa position dans la conversation
    const assistantMessageIndex = this.messages.length - 1;

    this.chatSubscription = this.apiService.sendMessage(question).subscribe({

      next: (chunk) => {

        // Ajouter progressivement chaque morceau
        this.messages[assistantMessageIndex].text += this.cleanChunk(chunk);
      },

      error: (error) => {

        console.error('Erreur API chat :', error);

        this.messages[assistantMessageIndex].text =
          'Désolé, une erreur est survenue lors de la communication avec le serveur.';

        this.isLoading = false;
      },

      complete: () => {

        this.isLoading = false;
      }
    });
  }

  private cleanChunk(chunk: string): string {

    return chunk
      .replace(/^data:\s?/gm, '')
      .replace(/\r/g, '');
  }

  ngOnDestroy(): void {

    this.isLoading = false;

    if (this.chatSubscription) {
      this.chatSubscription.unsubscribe();
    }
  }
}