Here's a **README.md** file for your **Scribble Game Project** to use on GitHub.  

---

# 🎨 Scribble Multiplayer Game  
A real-time **multiplayer drawing and guessing game** built with **Java**, featuring **multithreading**, **JDBC (MySQL)**, **Swing (GUI)**, and **WebSockets** for live communication.  

---

## 🚀 Features  
✅ **Multiplayer Support** – Play with multiple users in real time.  
✅ **Turn-Based Drawing** – Only one player can draw at a time.  
✅ **Word Selection System** – The drawer selects from three randomly generated words.  
✅ **Live Guessing & Chat** – Players can guess the word and chat in real time.  
✅ **Scoring System** – Points awarded for correct guesses.  
✅ **Cheat Prevention** – Words are hidden from guessers.  
✅ **Automatic Drawer Selection** – When a round ends, a new drawer is chosen randomly.  

---

## 🛠️ Technologies Used  
- **Java (Swing, AWT, JDBC, Multithreading, WebSockets)**
- **MySQL** – Used for storing words and scores.  
- **Sockets** – For real-time communication between server and clients.  

---

## 📂 Project Structure  
```
📦 ScribbleGame
 ┣ 📂 src
 ┃ ┣ 📂 client
 ┃ ┃ ┣ 📜 Client.java
 ┃ ┃ ┣ 📜 GameWindow.java
 ┃ ┃ ┗ 📜 DrawingBoard.java
 ┃ ┣ 📂 server
 ┃ ┃ ┣ 📜 Server.java
 ┃ ┃ ┗ 📜 ClientHandler.java
 ┃ ┣ 📂 database
 ┃ ┃ ┗ 📜 scribble_db.sql
 ┃ ┗ 📂 assets
 ┃ ┃ ┗ 📜 (Images, icons, etc.)
 ┣ 📜 README.md
 ┣ 📜 .gitignore
 ┗ 📜 run_game.bat
```
---

## 🔧 Installation & Setup  

### **1️⃣ Setup MySQL Database**
1. Install MySQL and create a database **scribble_db**.
2. Run the provided **scribble_db.sql** file to create the **words** table.  
3. Insert word data manually:  
   ```sql
   CREATE TABLE words (
       id INT AUTO_INCREMENT PRIMARY KEY,
       word VARCHAR(255) NOT NULL
   );

   INSERT INTO words (word) VALUES 
   ('Tree'), ('Cat'), ('House'), ('Rocket'), ('Sun');
   ```

---

### **2️⃣ Run the Server**
1. Open a terminal in the project directory.
2. Compile and run the server:  
   ```bash
   cd src
   javac server/Server.java
   java server.Server
   ```
   The server will start on **port 5000**.

---

### **3️⃣ Run the Client**
1. Open another terminal and compile the client:  
   ```bash
   javac client/Client.java
   java client.Client
   ```
2. When prompted, enter a **username** and start playing!

---

## 🎮 How to Play?  
1️⃣ **One player is assigned as the drawer.**  
2️⃣ The drawer selects a word and starts drawing.  
3️⃣ Other players **guess the word** in the chat.  
4️⃣ The **first correct guess** wins points!  
5️⃣ A new drawer is **automatically assigned**, and the game continues.  

---

## 🏆 Scoring System  
- **Correct Guess:** 🏅 +10 points  
- **Drawer Bonus (if guessed):** ✏️ +5 points  
- **Wrong Guess:** ❌ No penalty  

---

## 💡 Future Improvements  
🔹 **Leaderboard System** – Track high scores.  
🔹 **Custom Room Creation** – Private matches with friends.  
🔹 **Power-ups & Hints** – Extra features for guessing.  

---

## 📝 License  
This project is open-source. Feel free to use and modify it! 🚀  

---

🔥 **Enjoy the game & happy coding!** 🎨💻
