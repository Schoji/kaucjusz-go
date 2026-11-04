---
name: figma-daily-summary
description: Build the Kaucjusz Go end-of-day summary deck in Figma (via the figwright MCP). Use when the author asks for a "prezentacja podsumowująca dzień", "podsumowanie dnia w Figmie", "prezentacja na koniec zajęć", or a deck like the previous daily summaries — from today's commits/diff plus what teammates (Feliks, Patryk, Mikołaj, …) did.
---

# Kaucjusz Go — daily summary deck in Figma

A 16:9 slide deck (1920×1080 frames) in the shared Figma file, one page per day,
in Polish, in the same visual style every time. This is documentation work, not
code — the AGENTS.md "no code" rule is not affected, but never commit anything.

## 0. Audience: NON-TECHNICAL

The deck is shown to non-technical people. **Never put code/API names on slides** —
no widget or function names (`showModalBottomSheet`, `SearchBar`, `StarBorder`…), no
"API", "endpoint", "OCR", "Firebase", "parsowanie", "review", "JWT" etc. Describe what
the user sees or gets, in plain Polish:

| Technical | On the slide |
|---|---|
| bottom sheet | panel po skanie / wysuwany panel od dołu |
| API / backend endpoints | serwer (np. „Serwer kaucjomatów”) |
| Stage: Review | do sprawdzenia |
| OCR | AI odczytuje paragony |
| parsowanie / walidacja kodu | odczyt kodów / sprawdzanie poprawności kodu |
| Firebase auth | logowanie w aplikacji |

Ticket ids (KGO-40) are fine as small eyebrows; the heading under them must be plain language.

## 0.5 Tell a story, not a feature list (feedback from the teacher, 04.11)

The teacher said she did not understand what the author's app slide was about. A list of
features next to random screenshots is not enough. Plan the deck strategically:

- **Order slides as a story:** what the app is for → what the user does today in the app
  (the flow) → what each person built for it → what is next. Teammate slides should point back
  to the step of the flow they support (e.g. Patryk's research = step „skanujesz paragon”).
- **Show the user's pipeline step by step.** For the author's app work, build a flow slide:
  phone screenshots left → right in the order the user sees them, each with a big step number
  and a one-line plain-language caption of what the user does / sees, e.g.
  1 „Wchodzisz w zakładkę Skanuj” → 2 „Celujesz aparatem w kod na paragonie” →
  3 „Klikasz przycisk aparatu” → 4 „Widzisz, ile punktów dostałeś”.
  Connect steps with simple arrows/chevrons (shapes or `›`, never emoji). Mark where the user taps.
  These step captions are the exception to the „no captions under images” rule — they are the content.
- If a screenshot for a step is missing, keep an empty phone frame with the step caption and ask for it.
- Before building, write the slide order + one-line purpose of each slide in chat as a plan
  if the day's content is unclear; otherwise just follow the story order above.

## 1. Gather content

- **Author's work:** `git log --since=midnight --stat`, plus `git diff` of uncommitted
  changes (the author often hasn't committed yet). Summarise *features* the user sees,
  not files. Also the author tells you in chat what they did — that wins.
- **Teammates:** the author tells you in chat (Feliks = makiety/UI w Figmie,
  Patryk = research, Mikołaj = backend). Do not invent details beyond what they said.
  When the author gives YouTrack ids (KGO-xx), fetch each with `youtrack-kaucjusz` `get_issue`
  for the summary and Stage (Review → „do sprawdzenia”, Develop → „w toku”).
- **Images:** pasted chat images live in
  `/private/tmp/claude-501/-Users-piotrwittig-Coding-kaucjusz-go/<session>/images/` (`.webp` or `.png`).
  Phone screenshots usually come pasted in chat; otherwise check `~/Downloads`
  (`ls -t ~/Downloads | head`; a 4032×3024 HEIC is a camera photo, not a screenshot).
  If a screenshot is missing, build the slide with an empty phone frame and ask for it.
- Figma's `import_image` takes only PNG/JPG/GIF, max 4096 px: convert into the scratchpad with
  `sips -s format png in.webp --out <scratchpad>/x.png`.
  App screenshots have a thin green border — crop it: `sips -c <h-10> <w-10> x.png --out x.png`.
  Emulator screenshots: crop to the phone only with `sips -c H W --cropOffset Y X`.

## 2. Figma files and page

- Two files are involved, both must have the Figwright plugin running:
  - **„Kaucjusz Materialy”** — the deck file (pages „Kaucjusz Go — podsumowanie DD.MM”).
  - **„Main”** — Feliks's app mockups (Page 1, components ~412×868 px).
- Always `list_files` first. If the deck file is missing, ask the author to open it and run
  Figwright there. With 2+ files open, `use_file` the one you work on (switch to „Main”
  only to export mockups: `save_screenshots` scale 2 into the scratchpad, then back).
- Look at the previous summary page first (`scan_nodes_by_types FRAME` on the page,
  `get_screenshot` scale 0.3–0.4).
- `add_page` named **`Kaucjusz Go — podsumowanie DD.MM`**, then `navigate_to_page`.
- Frames: `create_frame` 1920×1080, `clipsContent: true`, at x = i·2020, y = 0,
  named `DD.MM · N — <temat>`. When inserting/removing a slide, shift x and renumber names.
- At the end, wire the deck so Play goes through all slides — a flow alone shows only the
  first frame. On every frame except the last, `set_reactions` (can go in one `batch`):
  `{ trigger: { type: "ON_CLICK" }, actions: [{ type: "NODE", destinationId: <next frame>,
  navigation: "NAVIGATE", transition: { type: "DISSOLVE", easing: { type: "EASE_OUT" }, duration: 0.3 } }] }`.
  Then `update_flows` on the page with the first frame, name `Podsumowanie DD.MM`.
  Re-wire after adding/removing slides.
- If the author deleted an element in Figma, do not re-create it — mention it at most.

## 3. Design tokens (sRGB 0–1)

| Token | Color | Use |
|---|---|---|
| bg | 0.9647, 0.9725, 0.9451 | base fill |
| accent | 0.1804, 0.4196, 0.1882 | eyebrow, numbers |
| dark | 0.0784, 0.1882, 0.1216 | titles, all other text |
| chip | 0.851, 0.906, 0.824 | number circles |
| card | white, stroke 0.8627, 0.898, 0.8392 @2px INSIDE, radius 24 | cards |
| phone stroke | dark @6px OUTSIDE, radius 36 | screenshots |

**Background (every frame)** — modern gradient: three fills (`set_fills`, can batch all frames):
1. SOLID bg
2. GRADIENT_RADIAL top-right glow: `gradientTransform [[1.111,0,-0.5],[0,0.625,0.469]]`,
   stops 0 → (0.68, 0.88, 0.66, a 0.8), 1 → same colour a 0
3. GRADIENT_RADIAL bottom-left glow: `gradientTransform [[1.25,0,0.4375],[0,0.704,-0.204]]`,
   stops 0 → (0.7, 0.88, 0.82, a 0.85), 1 → same colour a 0

Font: Roboto (Bold / Regular). Margins: x 128 … 1792.
- Eyebrow: Bold 28, accent, UPPERCASE, at (128, 152) — e.g. `PODSUMOWANIE · 04.11`, `PIOTR · APLIKACJA FLUTTER`.
  A second eyebrow inside the slide (e.g. `WBUDOWANE, A KIEDYŚ PISAŁEM TO RĘCZNIE`, `ZROBIONE`, `CO DALEJ`)
  is fine to group a list.
- Title: Bold 80, dark, at (128, 210); one line, or two with `\n`.
- Card title: Bold 44; list item: Bold 32–34 next to a 56–72 px chip with the number.
- **Chips:** `create_frame` 56×56 (or 64/72), radius = half, chip fill,
  `autoLayout { layoutMode: HORIZONTAL, primaryAxisAlignItems: CENTER, counterAxisAlignItems: CENTER }`,
  then in the next batch a Bold 26–32 accent text child with the number — centres itself, no resize needed.

**No secondary / muted text.** No grey description lines, subtitles or captions under titles
or images. Make the headings themselves informative. Avoid characters that render as emoji (e.g. `↔`).

**Mascots („stworki”)** — butelka-maskotka images the author provides (happy with thumbs up,
shrug with „…”, sad with retry arrow). Remove the white background with a border flood fill
(PIL, near-white > 228 → transparent, crop to bbox, upscale small ones 3× LANCZOS) and
sprinkle them into empty corners: happy on the overview next to the title (~290×320 at x 1500, y 150),
thinking on a research slide, sad on the bugs slide. Keep low-res ones small.

## 4. Slide set (adapt to the day)

1. **Przegląd dnia** — eyebrow `PODSUMOWANIE · DD.MM`, one-line title, one „fresh” glass card per person,
   y 500, h 330. 3 people: x 128 / 696 / 1264, w 528. 4 people: x 128 / 552 / 976 / 1400, w 392.
   Card: radius 36 + `cornerSmoothing 0.6`; fill GRADIENT_LINEAR top→bottom
   (`gradientTransform [[0,1,0],[-1,0,1]]`, white a 0.85 → per-person pastel a 0.95: mint 0.78/0.93/0.76,
   lime 0.87/0.94/0.70, teal 0.74/0.91/0.88, sage 0.84/0.90/0.78); stroke white @2px INSIDE opacity 0.9;
   DROP_SHADOW (0.08, 0.3, 0.12, a 0.12), offset y 20, radius 50.
   Inside: 72 px avatar circle (card+36, card+36; gradient (0.25,0.55,0.27) → dark,
   `[[0.7,0.7,-0.2],[-0.7,0.7,0.5]]`) with a white Bold 32 initial; name Bold 32 dark at (card+124, card+38);
   area UPPERCASE Bold 20 accent, letterSpacing 8 %, at (card+124, card+80); 2-line Bold 44 title at (card+36, card+180).
2. **Author's app work — ONE slide** (do not split into two). Prefer the step-by-step user flow
   from section 0.5 (screenshots in order, numbered step captions under each phone) over a bare feature list. Title left, optional group eyebrow
   at y 460, numbered list (chips x 128, text Bold 34 at x 216, rows every 100 px from y 530),
   up to 3 phone screenshots on the right: 300×652 at x 828 / 1160 / 1492, y 214
   (frame with radius 36, `clipsContent`, phone stroke; image imported as child, FILL).
   With 2 screenshots: 350×760 at x 1052 / 1442, y 150.
   The author likes the angle „after 2 years of Flutter I still find built-ins I used to write by hand” —
   phrase it in plain language (what the thing does), never the widget name.
3. **Teammate slides** (one per person):
   - Feliks — mockups exported from „Main”: 3 phones 285×600 at x 128 / 445 / 762, y 360 (FIT),
     3 numbered Bold 32 headings: chips x 1150, text x 1230, y 420 / 610 / 800.
   - **Bugs slide** („śmianie się z bugów Piotrka”) — title like „Nie wszystko wyszło za pierwszym razem”,
     cropped bug screenshots (radius 24) left, short funny numbered names right (chips x 1360, text x 1440).
   - Patryk (research) — 3 white cards 528×320 at y 460 with a 64 chip and a 2-line Bold 44 title.
   - Mikołaj (tickets) — 2×2 white cards 816×230 (x 128 / 976, y 400 / 662):
     `KGO-xx · DO SPRAWDZENIA` eyebrow + Bold 44 plain-language title.
4. **Status i kolejne kroki** — eyebrows `ZROBIONE` (x 128) and `CO DALEJ` (x 900) at y 340;
   rows y 400 / 530 / 660 / 790, chip 72, text Regular 40 (left text x 232, right chips x 900, text x 1004).
   Right column = next steps per person as `Imię: zadanie` — the author usually gives them; if not,
   propose and say so. Optional `RYZYKA` eyebrow at y 900 + one Regular 30 line separated by ` · `
   (your proposals from the diff — tell the author).

## 5. figwright gotchas

- `create_text` has no width; for wrapping, `resize_nodes` then `set_text_properties { textAutoResize: "HEIGHT" }`.
  Prefer explicit `\n` line breaks instead.
- `set_corner_radius` takes `radius`, not `cornerRadius`.
- `get_design_context` refuses a PAGE id — pass a frame id. `search_nodes type=FRAME` on a big
  page overflows — use `scan_nodes_by_types` with a `root`, or screenshot the page at scale 0.1.
- Ops inside one `batch` cannot reference ids created in the same batch: create frames first,
  then fill them in the next batch using the returned ids.
- Always verify with `get_screenshot` (scale 0.4) on every frame and fix overlaps
  (text running into images, chips without numbers) before reporting back.

## 6. Report

Reply in Polish, briefly: page name, slide list, what was inferred (next steps, risks,
teammate descriptions) and what is still missing (e.g. screenshots not yet provided).
