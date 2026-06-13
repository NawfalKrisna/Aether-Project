# 🚀 Aether Project

### 📂 Aplikasi Manajemen Surat (Java Swing)

<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0F172A&height=180&section=header&text=Aether%20Project&fontColor=ffffff&fontSize=38&animation=fadeIn" />
</p>

---

## ✨ Overview

**Aether Project** adalah aplikasi desktop berbasis **Java 8 (Swing)** untuk mengelola data surat secara efisien.

Fitur utama:

* 📥 Surat Masuk
* 📤 Surat Keluar
* 📊 Dashboard statistik
* 📄 Export PDF
* 📎 Upload file dokumen

Aplikasi menggunakan **SQLite** sebagai database lokal dan tampilan modern dengan **FlatLaf**.

---

## 🔥 Features

* 📊 Dashboard ringkasan data
* 📥 CRUD Surat Masuk
* 📤 CRUD Surat Keluar
* 📎 Upload & manajemen file
* 📄 Export ke PDF (iText)
* 🔎 Filtering & sorting data
* 🪟 Fullscreen custom window (tanpa title bar)
* 🎨 UI modern dengan FlatLaf
* 🔒 Tombol keluar dengan konfirmasi

---

## 🧱 Tech Stack

* ☕ Java 8
* 🖼️ Java Swing
* 🎨 FlatLaf
* 🗄️ SQLite (JDBC)
* 📄 iText PDF

---

## 📁 Project Structure

```bash id="z2m7kp"
Aether Project/
│
├── src/
│   ├── app/
│   │   └── App.java
│   │
│   ├── dao/
│   │   ├── SuratMasukDAO.java
│   │   └── SuratKeluarDAO.java
│   │
│   ├── database/
│   │   ├── DatabaseConnection.java
│   │   └── DatabaseInitializer.java
│   │
│   ├── model/
│   │   ├── Surat.java
│   │   ├── SuratMasuk.java
│   │   └── SuratKeluar.java
│   │
│   ├── service/
│   │   ├── FilterService.java
│   │   ├── SortService.java
│   │   └── PdfExporter.java
│   │
│   ├── utils/
│   │   ├── DateUtil.java
│   │   └── FileUploadUtil.java
│   │
│   └── view/
│       ├── DashboardFrame.java
│       ├── HomePanel.java
│       ├── SuratMasukPanel.java
│       ├── SuratKeluarPanel.java
│       ├── ExportPdfPanel.java
│       ├── AboutPanel.java
│       ├── TambahSuratMasukDialog.java
│       └── TambahSuratKeluarDialog.java
│
├── lib/
│   ├── flatlaf-3.4.1.jar
│   ├── itextpdf-5.5.13.4.jar
│   └── sqlite-jdbc-3.53.2.0.jar
│
├── surat.db
├── uploads/
├── bin/
├── out/
└── README.md
```

---

## ⚙️ How to Run

### 🔹 Compile (Recommended)

```bash id="d1avqx"
javac -encoding UTF-8 --release 8 -cp "lib/*" -d out -sourcepath src src/app/App.java
```

### 🔹 Compile

```bash id="d1avqx"
javac -cp "lib/*" -d bin src/**/*.java
```

### 🔹 Run

```bash id="6g8b7u"
java -cp "bin;lib/*" app.App
```

> 💡 Windows gunakan `;`
> 💡 Linux/Mac gunakan `:`

---

## 🧠 Architecture

Project ini menggunakan pendekatan modular:

* **Model** → representasi data
* **DAO** → akses database
* **Service** → logic (filter, sort, export)
* **Utils** → helper functions
* **View** → UI (Swing)

---

## 🔥 Special Implementation

* Custom window tanpa border (`setUndecorated`)
* Drag window manual
* Navigasi halaman dengan `CardLayout`
* Export data ke PDF
* SQLite embedded database

---

## 🌟 Future Improvements

* 🔍 Search real-time
* 👤 Multi-user login
* 🌙 Dark mode
* 📊 Grafik statistik
* ☁️ Backup database

---

## 👨‍💻 Development Team

**Aether Team** 🚀

| Name                      | NIM          | Role      |
| ------------------------- | ------------ | --------- |
| Wisnu Septa Harianto Putra| 242502040041 | Backend   |
| Hana Joma Naomi           | 242502040050 | Frontend  |
| Nawfal Krisna Aghafazli   | 242502040070 | Full Stack|
| Ridhoi Wahyu Saputra      | 242502040115 | UI / UX   |

---

<p align="center">
  💙 Built with Java Swing
</p>
