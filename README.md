# 🎨 Scribble Multiplayer Game

A real-time **multiplayer drawing and guessing game** built with **Java**, featuring **multithreading**, **JDBC (MySQL)**, **Swing (GUI)**, and **WebSockets** for live communication.

---

## 🚀 Features

✅ **Multiplayer Support** – Play with multiple users in real time.  
✅ **Turn-Based Drawing** – Only one player can draw at a time.  
✅ **Word Selection System** – The drawer selects from three randomly generated words.  
✅ **Live Guessing & Chat** – Players can guess the word and chat in real time.  
✅ **Cheat Prevention** – Words are hidden from guessers.  
✅ **Automatic Drawer Selection** – When a round ends, a new drawer is chosen randomly.

---

## 🛠️ Technologies Used

- **Java (Swing, AWT, JDBC, Multithreading, WebSockets)**
- **MySQL** – Used for storing words.
- **Sockets** – For real-time communication between server and clients.

---

## 📂 Project Structure

```
📦 ScribbleGame
 ┣ 📂 src
 ┃ ┣ 📂 client
 ┃ ┃ ┣ 📄 Client.java
 ┃ ┃ ┣ 📄 GameWindow.java
 ┃ ┃ ┗ 📄 DrawingBoard.java
 ┃ ┣ 📂 server
 ┃ ┃ ┣ 📄 Server.java
 ┃ ┃ ┗ 📄 ClientHandler.java
 ┃ ┣ 📂 database
 ┃ ┃ ┗ 📄 scribble_db.sql
 ┃ ┗ 📂 assets
 ┃ ┃ ┗ 📄 (Images, icons, etc.)
 ┣ 📂 lib
 ┃ ┗ 📄 (Required JAR files)
 ┣ 📄 README.md
 ┣ 📄 .gitignore
 ┗ 📄 run_game.bat
```

---

## 🛠️ Setup Instructions for IntelliJ IDEA

### **1️⃣ Clone the Repository**
```bash
git clone https://github.com/yourusername/ScribbleGame.git
cd ScribbleGame
```

### **2️⃣ Open the Project in IntelliJ IDEA**
1. Open **IntelliJ IDEA**.
2. Click **Open** and select the **src** folder inside the cloned repository.

### **3️⃣ Setup MySQL Database**
1. Install MySQL and create a database **scribble_db**.
2. Inside the `database` folder, locate `scribble_db.sql`.
3. Execute the SQL file in your MySQL database.

### **4️⃣ Update Database Credentials**
1. Open `database.Database.java`.
2. Locate the **database password** and update it to match your MySQL password.
3. Repeat this in `server.Server.java`.

### **5️⃣ Add Required JAR Files**
1. Go to **File** → **Project Structure** → **Libraries**.
2. Click on the **➕ (Plus button)** and select **Java**.
3. Select all JAR files inside the `lib` folder.
4. Click **Apply** and **OK**.

### **6️⃣ Run the Server**
1. In IntelliJ, open `Server.java`.
2. Click **Run** ▶️.
3. Once the terminal displays **Database connected!**, proceed.

### **7️⃣ Run the Client**
1. Open and run `LoginScreen.java`.
2. Enter a **player name**.
3. Without stopping the previous instance, run `LoginScreen.java` again to add another player.

---

## 🎮 How to Play?

1️⃣ **One player is assigned as the drawer.**  
2️⃣ The drawer selects a word and starts drawing.  
3️⃣ Other players **guess the word** in the chat.  
4️⃣ A new drawer is **automatically assigned**, and the game continues.

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

