<h1><b>System Monitoringu Jednostek Morskich </b></h1>
[![Watch the video](https://img.youtube.com/vi/BxgV89NuF4I/maxresdefault.jpg)](https://www.youtube.com/watch?v=BxgV89NuF4I)

System Monitoringu Jednostek Morskich to aplikacja umożliwiająca monitorowanie jednostek morskich w czasie rzeczywistym. Wykorzystuje różne technologie, w tym język Java, do dostarczenia funkcji takich jak śledzenie statków,generowanie wkresów oraz raportów,analizą danych,obserwacji historii statków, zarządzanie użytkownikami,udostępnianie interfejsu REST API itd. Poniżej znajduje się krótki opis kluczowych aspektów projektu.

Wykorzystane Technologie oraz ważne aspekty projektu:

    - Java 8: Główny język programowania, który napędza aplikację i umożliwia jej działanie.
    - Spring Boot 2.7.5: Aplikacja została zbudowana przy użyciu frameworka Spring Boot, co pozwoliło na szybki rozwój, testowanie aplikacji i łatwą integrację.
    - WebSocket i STOMP: Aktualizacje położenia statków w czasie rzeczywistym są osiągane dzięki technologii WebSocket i protokołowi STOMP, co zapewnia dynamiczne dane dla użytkowników.
    - Baza Danych PostgreSQL: Wszystkie przetworzone dane są przechowywane w bazie danych PostgreSQL, a zarządzanie nimi jest efektywne dzięki frameworkowi Hibernate ORM.
    - Dokumentacja Swagger: Interfejs API jest udokumentowany za pomocą Swaggera, co zapewnia interaktywny sposób dla deweloperów do eksplorowania dostępnych punktów końcowych i testowania API.
    - Zabezpieczenia JWT: Aplikacja używa tokenów JWT (JSON Web Tokens) do autoryzacji użytkowników i zabezpieczenia dostępu do zasobów.
    - Interfejs REST API: Aplikacja udostępnia interfejs REST API, który umożliwia komunikację z aplikacją za pomocą standardowych operacji HTTP.
   
Główne Funkcje: 

    - Śledzenie Statków w Czasie Rzeczywistym: Użytkownicy mogą śledzić jednostki morskie w czasie rzeczywistym, otrzymując aktualizacje ich położenia.Dodatkowo, aplikacja umożliwia obliczanie prędkości i dystansu na       podstawie zmian położenia statku, co pozwala użytkownikom monitorować ruch statków.
    - Zarządzanie Użytkownikami: System pozwala na zarządzanie użytkownikami, w tym tworzenie,nadawanie uprawnień, usuwanie i aktualizację ich kont.
    - Powiadomienia Email: Użytkownicy otrzymują powiadomienia e-mail, gdy nowe jednostki morskie pojawiają się na monitorowanym obszarze.
    - Przypisywanie Statków do Obserwacji: Użytkownicy mają możliwość przypisywania statków do obserwacji. Gdy nowa jednostka morska pojawi się w obszarze obserwacji, użytkownicy otrzymają powiadomienia e-mail z `          informacjami na jej temat.
    - Śledzenie Historii:Aplikacja możliwia użytkownikom śledzenie historii ruchu jednostek morskich. Dzięki temu użytkownicy mogą analizować wcześniejsze trasy i położenie statków.
    - Sprawdzanie Aktualnej Pogody:System Monitoringu Jednostek Morskich pozwala użytkownikom sprawdzać aktualne dane pogodowe w miejscu, gdzie znajduje się dany statek.
    - Pobieranie Zdjęć z Google:użytkownicy mają możliwość pobierania zdjęć z Google na temat statku, wykorzystując jego numer identyfikacyjny MMSI (Maritime Mobile Service Identity).
    Intuicyjna Dokumentacja: Dzięki Swaggerowi, deweloperzy mogą łatwo zrozumieć dostępne funkcje API i testować je bez konieczności implementacji klienta.
    - Przetwarzanie Danych dla Wykresów i Raportów: Aplikacja gromadzi dane statystyczne dotyczące ruchu jednostek morskich, które mogą być wykorzystane do generowania wykresów i raportów. Użytkownicy mają możliwość        analizy danych i tworzenia raportów na podstawie zebranych informacji.
