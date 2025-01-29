**NetFlow ve SNMP: Ağ Yönetimi ve Güvenliği İçin Kapsamlı Kurulum Rehberi**

Ağ yöneticileri için **NetFlow ve SNMP**, ağ trafiğini izleme, analiz etme ve yönetme açısından kritik öneme sahiptir. Bu makalede, NetFlow ve SNMP’nin ne olduğu, nasıl çalıştığı, kurulumu, veri analizi ve güvenlik önlemleri detaylı bir şekilde ele alınacaktır.

---

## **1. SNMP Nedir?**

### **1.1 SNMP'nin Çıkışı ve Versiyonları**

SNMP (**Simple Network Management Protocol**) 1988'de geliştirildi ve ağ cihazlarını izlemek ve yönetmek için kullanılan bir protokol olarak yaygınlaştı.

- **SNMPv1:** İlk sürüm, güvenlik açıkları nedeniyle zayıf kabul edilir.
- **SNMPv2:** Performans ve hata yönetimi geliştirildi, ancak güvenlik eksikleri devam etti.
- **SNMPv3:** Kimlik doğrulama ve şifreleme eklenerek güvenlik güçlendirildi.

### **1.2 SNMPv3 Güvenlik Özellikleri**

- **Kimlik Doğrulama:** HMAC-MD5 veya HMAC-SHA kullanarak kullanıcı doğrulaması yapar.
- **Şifreleme:** AES veya DES algoritmaları ile verileri şifreleyerek güvenliği artırır.
- **Veri Bütünlüğü:** Mesajların değiştirilip değiştirilmediğini algılar.

### **1.3 SNMPv3 Güvenlik Açıkları ve Riskleri**

- **Yanlış Yapılandırma:** SNMPv3’ün doğru yapılandırılmaması, şifreleme ve kimlik doğrulamanın devre dışı bırakılmasına yol açabilir.
- **Brute Force Saldırıları:** Zayıf kullanıcı adı ve şifreler brute force saldırılarına açıktır.
- **Side-Channel Saldırıları:** Şifreleme algoritmalarına yönelik saldırılar güvenliği riske atabilir.

### **1.4 SNMP ile Cihaz Bilgi Toplama Örnekleri**

- Router CPU, RAM ve bant genişliği kullanımı.
- Ağ switch'lerinin port durumu.
- Sunucu sistemlerinin çalışma süresi.

### **1.5 SNMP ile Cihaz Yönetimi Örnekleri**

- Router yapılandırmalarını değiştirme.
- Anahtarlar üzerindeki portları aktif/pasif hale getirme.
- SNMP trap mekanizması ile hata raporlaması.

---

## **2. NetFlow ve SNMP Arasındaki Farklar**

### **2.1 NetFlow Nedir?**

NetFlow, Cisco tarafından geliştirilen ve ağ trafiği akışlarını analiz eden bir protokoldür. Kaynak IP, hedef IP, port numaraları gibi verileri toplar.

- **Trafik Analizi:** Hangi uygulamaların bant genişliği kullandığını analiz eder.
- **Güvenlik İzleme:** Şüpheli trafik akışlarını tespit eder.

### **2.2 SNMP ve NetFlow'un Temel Farkları**

| Özellik       | SNMP                              | NetFlow                         |
| ------------- | --------------------------------- | ------------------------------- |
| **Kapsam**    | Donanım ve performans izleme      | Trafik akışlarının analizi      |
| **Veri Türü** | Anlık cihaz verisi                | Trafik akış verisi              |
| **Protokol**  | Sorgu tabanlı                     | Akış tabanlı                    |
| **Güvenlik**  | SNMPv3 ile şifreleme ve doğrulama | Şifreleme genellikle uygulanmaz |

### **2.3 Kullanım Alanları**

- **SNMP:**
  - Router, switch ve sunucu yönetiminde kullanılır.
  - Ağ cihazlarının durumunu ve performansını izler.
- **NetFlow:**
  - Bant genişliği analizi ve trafik trendlerinin belirlenmesinde kullanılır.
  - Şüpheli veya istenmeyen trafik akışlarının tespitinde etkilidir.

### **2.4 Veri Toplama ve Analiz Yöntemleri**

- **SNMP:**

  - Anlık cihaz bilgilerini sorgulamak için kullanılır.
  - PRTG veya SolarWinds gibi yazılımlar aracılığıyla grafiksel raporlar sunar.

- **NetFlow:**

  - Router veya switch üzerinden trafik akışı bilgisi toplar.
  - Wireshark veya PRTG ile analiz edilebilir.

### **2.5 Performans ve Güvenlik Karşılaştırması**

- **SNMP Performansı:**

  - Hafif bir protokol olduğundan cihaz performansını fazla etkilemez.
  - Ancak zayıf yapılandırma durumunda güvenlik açıkları oluşturabilir.

- **NetFlow Performansı:**

  - Yoğun veri analizi gerektirdiğinden yüksek trafik altında cihaz performansını etkileyebilir.
  - Güvenlik açısından varsayılan olarak şifreleme sunmaz.

---

## **3. Kurulum ve Konfigürasyon Rehberi**

### **3.1 SNMP Kurulum ve Konfigürasyonu**

#### **Windows Üzerinde SNMP Kurulumu:**

1. **Windows Özelliklerini Aç:**

   - "Denetim Masası > Programlar > Windows Özelliklerini Aç veya Kapat" yolunu izleyin.
   - "SNMP Hizmeti" kutusunu işaretleyerek etkinleştirin.

2. **PowerShell Kullanarak Kurulum:**

   ```powershell
   dism /online /add-capability /capabilityname:SNMP.Client~~~~0.0.1.0
   Start-Service SNMP
   ```

3. **SNMP Hizmetini Yapılandırma:**

   - `services.msc` ile SNMP Hizmeti'ni bulun ve sağ tıklayarak "Özellikler"i seçin.
   - **Güvenlik Sekmesi:** Topluluk string’lerini ("public/private") ve yetkilendirilmiş IP adreslerini ekleyin.

#### **Linux (Ubuntu) Üzerinde SNMP Kurulumu:**

1. **SNMP ve SNMPD Kurulumu:**

   ```bash
   sudo apt update
   sudo apt install snmp snmpd
   ```

2. **SNMPD Konfigürasyonu:**

   - `/etc/snmp/snmpd.conf` dosyasını düzenleyerek topluluk string’i ekleyin:
     ```
     rocommunity public 127.0.0.1
     ```
   - Hizmeti yeniden başlatın:
     ```bash
     sudo systemctl restart snmpd
     ```

3. **SNMP Testi:**

   ```bash
   snmpwalk -v2c -c public localhost
   ```

#### **Cisco Router ve Switch Üzerinde SNMP Yapılandırması:**

1. **SNMP Topluluk String’i Tanımlayın:**

   ```bash
   conf t
   snmp-server community public RO
   snmp-server community private RW
   ```

2. **SNMP Versiyonunu Belirleyin:**

   ```bash
   snmp-server host 192.168.1.100 version 3 priv USERNAME
   ```

3. **SNMP Hizmetini Etkinleştirin:**

   ```bash
   snmp-server enable
   ```

---

### **3.2 NetFlow Kurulum ve Konfigürasyonu**

#### **Cisco Router Üzerinde NetFlow Yapılandırması:**

1. **NetFlow'u Aktif Edin:**

   ```bash
   conf t
   interface GigabitEthernet0/1
   ip flow ingress
   ip flow egress
   exit
   ```

2. **NetFlow Export Sunucusunu Belirleyin:**

   ```bash
   ip flow-export destination <PRTG_IP> 2055
   ip flow-export version 9
   ```

3. **NetFlow Çıkışlarını Kontrol Edin:**

   ```bash
   show ip flow export
   ```

#### **Windows Üzerinde NetFlow Kullanımı (PRTG Kullanarak):**

1. **PRTG'yi İndirin ve Kurun:**

   - Resmi web sitesinden PRTG Network Monitor yazılımını indirip kurun.

2. **NetFlow Sensörünü Ekleyin:**

   - PRTG arayüzünde, cihazlar sekmesine gidin ve NetFlow için sensör ekleyin.
   - **NetFlow v9 Sensor** seçeneğini seçin.

3. **Router Üzerindeki Konfigürasyonu Test Edin:**

   - NetFlow trafiğinin PRTG’ye ulaşıp ulaşmadığını kontrol edin.

#### **Wireshark ile NetFlow ve SNMP Trafiği İzleme:**

1. **Wireshark Kurulumu:**

   - Wireshark’ı resmi sitesinden indirip yükleyin.

2. **Filtreleme:**

   - NetFlow trafiği için:
     ```
     udp.port == 2055
     ```
   - SNMP trafiği için:
     ```
     snmp
     ```

---

### **3.3 PRTG'nin Varsayılan Güvenlik Açıkları ve Çözümleri**

#### **1. Varsayılan Şifre Kullanımı**

- **Açıklama:** PRTG'nin varsayılan admin şifresi basit ve tahmin edilebilirdir.
- **Çözüm:**
  - Admin hesabını güçlü bir şifre ile değiştirin.
  - **Setup > Account Settings > User Accounts** sekmesine giderek şifreyi güncelleyin.

#### **2. Web Arayüzüne Yetkisiz Erişim**

- **Açıklama:** Tüm IP adresleri PRTG'nin web arayüzüne erişebilir.
- **Çözüm:**
  - **Setup > System Administration > User Interface** sekmesine gidin.
  - "Allow Web Interface Access" seçeneğini yalnızca belirli IP'lerle sınırlandırın.

#### **3. Şifrelenmemiş Trafik**

- **Açıklama:** PRTG varsayılan olarak HTTP üzerinden çalışır.
- **Çözüm:**
  - **Setup > System Administration > Web Server** sekmesine girerek HTTPS kullanımını etkinleştirin.

#### **4. SNMP Varsayılan Topluluk Dizgileri**

- **Açıklama:** "public" veya "private" gibi varsayılan topluluk string’leri kolayca tahmin edilebilir.
- **Çözüm:**
  - Güçlü ve rastgele topluluk string’leri belirleyin.
  - SNMP erişimini belirli IP adresleriyle sınırlayın.

#### **5. Gereksiz Sensörlerin Aktif Olması**

- **Açıklama:** Gereksiz sensörler fazladan yük oluşturabilir ve veri kaybına yol açabilir.
- **Çözüm:**
  - Aktif sensörleri düzenli olarak gözden geçirin ve kullanılmayanları devre dışı bırakın.

