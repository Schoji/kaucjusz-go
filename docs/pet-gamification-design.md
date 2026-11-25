# Kaucjusz Go – Pomysł: Zabawa jako mnożnik + Pasywne XP za nakarmienie

## Problem
Gdyby XP wpadało wyłącznie w momencie karmienia (nawet z mnożnikiem z humoru), gracz mógłby ignorować Kaucjusza cały dzień, wejść raz na 2 dni na chwilę, podbić humor zabawą tuż przed nakarmieniem, zgarnąć mnożnik i znowu wyłączyć aplikację. Utrzymywanie wysokiego humoru i sytości w czasie nie miałoby żadnego znaczenia.

---

## Rozwiązanie

### 1. Zabawa / Humor jako mnożnik XP
Zabawa jest darmowa (żeby był powód wejść do aplikacji nawet bez nowych punktów ze skanów). Poziom humoru (`mood`) działa jako **mnożnik XP**:

| Humor (`mood`) | Stan | Mnożnik XP |
| :---: | :---: | :---: |
| **`80–100`** | `happy` 😄 | **`×1.5`** |
| **`40–79`** | `neutral` 😐 | **`×1.0`** |
| **`0–39`** | `hungry_sad` 😢 | **`×0.7`** |

*(Przykładowe aktywności podbijające humor: szybkie głaskanie z cooldownem lub krótka 15-sekundowa mini-gra.)*

---

### 2. Pasywne XP powyżej progu nakarmienia
Dopóki Kaucjusz jest nakarmiony **powyżej ustalonego progu** (np. `hunger >= 50`), **pasywnie nabija XP w czasie** — a aktualny humor dodatkowo ten przyrost mnoży:

* **`hunger >= 50` i `mood >= 80` (`happy`)** → przyspieszone pasywne XP (np. bazowe XP/h × mnożnik `1.5`),
* **`hunger >= 50` i `mood 40–79` (`neutral`)** → normalne pasywne XP (bazowe XP/h × `1.0`),
* **`hunger < 50` lub `mood < 40`** → **`0` pasywnego XP** (głodny albo smutny Kaucjusz przestaje rosnąć).

### Dlaczego to działa?
* Nie opłaca się już bawić „tylko na sekundę przed nakarmieniem”, bo zaniedbując peta przez dzień traci się pasywne XP z wielu godzin.
* Gracz ma powód zaglądać do aplikacji w ciągu dnia, żeby podbić humor i utrzymać Kaucjusza w trybie `happy`, nawet gdy pet jest już najedzony.
* Pasywne XP samo naturalnie się zatrzymuje, gdy sytość lub humor spadną poniżej progu (więc nie da się farmić XP bez zaglądania do apki).
