# DAY30 — Web Automation: Dynamic Element Handling & Parallel Execution

## 1. Overview

Project ini merupakan hasil pembelajaran dan implementasi **DAY30** pada automation testing menggunakan **Java, Selenium WebDriver, TestNG, dan Gradle**.

Fokus utama project:

1. **Dynamic Element Handling** untuk menghadapi element/komponen web yang dapat berubah, muncul secara kondisional, berada di dalam iframe, atau membutuhkan synchronization sebelum dapat berinteraksi.
2. **Parallel Execution** menggunakan TestNG untuk menjalankan test method secara bersamaan sehingga waktu eksekusi dapat lebih efisien.
3. **Evidence & Reporting** sebagai bukti hasil implementasi melalui rekaman video dan laporan PDF.


---

## 2. Teknologi & Dependency

Project menggunakan **Gradle** sebagai build tool dengan dependency utama:

- Java
- Selenium Java `4.47.0`
- WebDriverManager `5.9.2`
- TestNG `7.10.2`
- Log4j2 `2.23.1`

Konfigurasi dependency dapat dilihat pada `build.gradle`.

---

## 3. Struktur Project

Struktur utama project:

```text
web_automation_DAY30/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── README.md
├── src/
│   ├── main/java/
│   │   ├── commons/BasePage.java
│   │   └── bookstore/
│   │       ├── LoginPage.java
│   │       ├── HomePage.java
│   │       └── CheckOutPage.java
│   └── test/
│       ├── java/
│       │   ├── core/
│       │   │   ├── BaseTest.java
│       │   │   ├── ConfigManager.java
│       │   │   └── DriverManager.java
│       │   ├── test/
│       │   │   ├── LoginPageTest.java
│       │   │   ├── HomePageTest.java
│       │   │   └── CheckOutPageTest.java
│       │   └── testing.xml
│       └── resources/config/
│           └── staging.properties
├── build/
│   ├── test-results/
│   └── reports/tests/test/
└── Hasil/
    ├── DAY30.mkv
    └── review dynamic element dan parallel execution.pdf
```

> Folder **`Hasil/`** digunakan khusus untuk evidence/dokumentasi hasil DAY30.

---

## 4. Standar Dynamic Element Handling

### 4.1 Prinsip

Handling dynamic element diterapkan agar automation tidak mudah gagal ketika halaman membutuhkan waktu untuk render atau element berubah secara runtime.

Standar yang digunakan pada project:

- Gunakan locator yang cukup spesifik dan mudah dipelihara.
- Hindari ketergantungan berlebihan pada index atau struktur DOM yang mudah berubah.
- Gunakan **explicit wait** untuk kondisi element.
- Tangani element yang bersifat **optional/conditional**, seperti popup atau advertisement.
- Sediakan fallback apabila interaksi normal tidak berhasil.
- Pastikan state browser dikembalikan ke `defaultContent()` setelah bekerja dengan iframe.

### 4.2 Implementasi pada Project

`BasePage.java` menjadi class utama untuk reusable handling.

Project juga memiliki mekanisme `closeAdIfPresent()` yang mencoba beberapa locator untuk menangani popup/iklan yang dapat muncul atau tidak muncul pada saat runtime.

Pendekatan yang digunakan meliputi:

- Multiple locator sebagai fallback.
- Pengecekan `isDisplayed()` sebelum interaksi.
- Scroll element ke area yang terlihat.
- Percobaan click Selenium terlebih dahulu.
- Fallback ke JavaScript click apabila click normal gagal.
- Pemeriksaan iframe yang tersedia.
- Selalu kembali ke `defaultContent()` setelah proses iframe selesai.

### 4.3 Catatan Standar

`Thread.sleep()` bukan mekanisme utama synchronization pada project. Terdapat delay `300 ms` pada helper setelah scroll untuk membantu stabilitas interaksi, sedangkan synchronization utama tetap menggunakan `WebDriverWait` dan `ExpectedConditions`.

---

## 5. Standar Parallel Execution

### 5.1 Konfigurasi

Parallel execution dikonfigurasi pada:

```text
src/test/java/testing.xml
```

Konfigurasi aktual:

```xml
<suite name="WebAutomationSuite"
       parallel="methods"
       thread-count="3">
```

Artinya TestNG menjalankan **test method secara parallel** dengan maksimal **3 thread** pada suite tersebut.

Suite juga menggunakan parameter browser:

```xml
<parameter name="browser" value="chrome"/>
```

Dan hanya group `smoke` yang dijalankan:

```xml
<groups>
    <run>
        <include name="smoke"/>
    </run>
</groups>
```

### 5.2 Isolation Browser / Driver

Untuk mendukung parallel execution, `DriverManager.java` menggunakan:

```java
private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
```

Pendekatan `ThreadLocal` memastikan setiap thread memiliki referensi WebDriver sendiri sehingga browser session tidak digunakan bersama oleh test yang berjalan secara bersamaan.

Setelah test selesai, driver ditutup dan thread-local dibersihkan menggunakan:

```java
driver.get().quit();
driver.remove();
```

### 5.3 Standar yang Harus Dijaga

Agar parallel execution tetap stabil:

- Test sebaiknya independen.
- Jangan berbagi WebDriver antar-thread.
- Hindari shared mutable state.
- Data test harus aman digunakan secara concurrent.
- Jumlah thread harus menyesuaikan kemampuan environment.
- Hasil test harus konsisten ketika dijalankan berulang.

---

## 6. Test Scenario yang Tersedia

Project memiliki tiga test class utama:

| Test Class | Jumlah Test | Group |
|---|---:|---|
| `LoginPageTest` | 1 | `smoke` |
| `HomePageTest` | 4 | `smoke` |
| `CheckOutPageTest` | 1 | `smoke` |
| **Total** | **6** | `smoke` |

Dengan konfigurasi `parallel="methods"` dan `thread-count="3"`, keenam test method dapat diproses secara parallel sesuai scheduling TestNG.

---

## 7. Hasil Execution

Berdasarkan test report yang tersimpan pada project:

- **Total test:** 6
- **Failures:** 0
- **Skipped:** 0
- **Success rate:** 100%
- **Duration pada report:** sekitar 1 menit 15 detik

Report Gradle tersedia pada:

```text
build/reports/tests/test/index.html
```

Hasil XML test juga tersimpan pada:

```text
build/test-results/test/
```

---

## 8. Evidence / Bukti DAY30

Semua bukti utama disimpan pada folder:

```text
Hasil/
```

### `Hasil/DAY30.mkv`

File ini merupakan **rekaman/video bukti pelaksanaan DAY30**, termasuk demonstrasi implementasi automation dan hasil execution.

### `Hasil/review dynamic element dan parallel execution.pdf`

File PDF merupakan **dokumen review pembelajaran dan implementasi** yang membahas dynamic element handling dan parallel execution.

Dengan demikian, folder `Hasil/` menjadi lokasi resmi untuk menyimpan evidence yang dapat digunakan saat review atau submission.

---

## 9. Cara Menjalankan Project

### Linux / macOS

```bash
./gradlew test
```

### Windows

```bat
gradlew.bat test
```

Konfigurasi suite TestNG akan otomatis menggunakan file:

```text
src/test/java/testing.xml
```

---

## 10. Standar Review DAY30

Checklist review:

- [ ] Dynamic element ditangani menggunakan locator yang cukup stabil.
- [ ] Popup/advertisement yang bersifat optional ditangani dengan aman.
- [ ] Interaksi iframe mengembalikan context ke `defaultContent()`.
- [ ] Parallel execution menggunakan konfigurasi TestNG yang jelas.
- [ ] WebDriver diisolasi menggunakan `ThreadLocal`.
- [ ] Test berjalan tanpa failure dan tanpa skipped test.
- [ ] Gradle test report tersedia.
- [ ] Rekaman `DAY30.mkv` tersedia di folder `Hasil/`.
- [ ] PDF review tersedia di folder `Hasil/`.

---

## 11. Kesimpulan

Implementasi DAY30 menunjukkan dua konsep penting dalam automation testing modern.

**Dynamic Element Handling** meningkatkan ketahanan test terhadap perubahan kondisi halaman, popup, iframe, dan timing element. Penggunaan explicit wait, beberapa locator fallback, serta mekanisme recovery membantu mengurangi flaky test.

**Parallel Execution** meningkatkan efisiensi execution dengan menjalankan test method secara bersamaan. Pada project ini, TestNG dikonfigurasi menggunakan `parallel="methods"` dengan `thread-count="3"`, dan isolasi browser didukung oleh `ThreadLocal<WebDriver>`.

Hasil execution yang terdokumentasi menunjukkan **6 test berhasil, 0 failure, 0 skipped, dan success rate 100%**. Evidence pelaksanaan tersedia di folder **`Hasil/`** dalam bentuk **`DAY30.mkv`** dan **PDF review**.

---

## 12. Author / Submission Note

README ini digunakan sebagai dokumentasi standar untuk project **DAY30 — Dynamic Element Handling & Parallel Execution**, sekaligus sebagai petunjuk lokasi source code, test configuration, report, dan evidence hasil pembelajaran.
