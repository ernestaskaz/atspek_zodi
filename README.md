# atspek_zodi
Atspėk žodį - žaidimas, kurio metu dvi komandos rungiasi kas geriau paaiškins atitinkamą lietuvišką žodį, nepanaudojant to žodžio. 
Žaidžiama iki 50 taškų.
teisingai paaiškintas žodis +1. Praleistas žodis -1 taškas.
Komandos keičiasi kas raundą. Komandą sudaro du nariai. Nariai kas antrą raundą keičia savo vaidmenį iš spėjančiojo į aiškintoją.
Vienas raundas trunka 60 sekundžių. Greitam testavimui - papildomas mygtukas "FOR TESTING", ignoruojantis laikmatį.
Programėlėje yra 1050 unikalių žodžių, sugeneruotų bot crawl pagalba iš Wiki populiariausių 2000 lietuviškų žodžių (atrinkti lengvesni žodžiai, be jungtukų ir pan.).
kiekvieną naują žaidimą žodžių ArrayList'as neprasideda nuo 0, taigi kiekvieną žaidimą bus tik unikalūs žodžiai (iki kol bus parodyti visi 1050 - tokiu atveju, index'as refreshinasi).
3 activity langai - komandų pavadinimai (jeigu laukai paliekami tušti, naudojami programėlės vardai ir pavadinimai), žaidimo veiksmo langas, žaidimo rezultatų langas su taškais, teisingais/neteisingais žodžių list'ais.
pasibaigus laikmačiui (60s), komanda turi paskutinį spėjimą. 
