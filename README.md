# AI Cybersecurity Assistant 🤖🔐

An AI-powered cybersecurity assistant that allows users to interact with an intelligent chatbot to ask cybersecurity-related questions and receive AI-generated responses.

The project combines an Angular frontend with a Spring Boot backend and integrates Google's Gemini API to generate responses in real time.

## 🚀 Features

- Interactive chatbot interface.
- AI-generated responses using the Gemini API.
- Real-time response streaming.
- Angular frontend connected to a Spring Boot REST API.
- Error handling for temporary AI service unavailability.

## 🛠️ Technologies

| Component | Technologies |
|---|---|
| Frontend | Angular, TypeScript, HTML, CSS |
| Backend | Java 21, Spring Boot |
| AI Integration | Google Gemini API |
| Communication | REST API, Server-Sent Events (SSE) |
| Version Control | Git, GitHub |

## 📁 Project Structure

```text
AI_Cyber/
├── backend/
│   └── src/
│       └── main/
│           ├── java/
│           └── resources/
│
├── frontend/
│   └── src/
│       └── app/
│
├── .gitignore
└── README.md
```

## ⚙️ Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/wissal122/AI_Cyber.git
cd AI_Cyber
```

### 2. Configure the Gemini API key

Create or obtain a Gemini API key from [Google AI Studio](https://aistudio.google.com/apikey).

Set the environment variable in your terminal:

**macOS / Linux:**

```bash
export GEMINI_API_KEY="YOUR_API_KEY"
```

Replace `YOUR_API_KEY` with your own API key.

Never commit your API key to GitHub.

### 3. Run the backend

Open a terminal:

```bash
cd backend
./mvnw spring-boot:run
```

The Spring Boot backend runs at:

`http://localhost:8080`

### 4. Run the frontend

Open another terminal:

```bash
cd AI_Cyber/frontend
npm install
npx ng serve
```

The Angular application runs at:

`http://localhost:4200`

## 🔗 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/hello` | Backend connectivity test |
| POST | `/api/chat` | Send a message and receive an AI-generated response stream |

## 🔮 Future Improvements

- Conversation history management.
- Integration of additional AI models.
- Retrieval-Augmented Generation (RAG).
- Cybersecurity knowledge base.
- Improved chatbot interface and user experience.

## 👩‍💻 Author

Wissal HAFDANE

Computer Engineering Graduate | AI & Software Development

[GitHub Profile](https://github.com/wissal122)