# About

Repozytorium zawiera rozszerzenie biblioteki JMetal w postaci algorytmu EMAS - agentowo-ewolucyjnego algorytmu do optymalizacji wielokryterialnej.
Jeżeli uzyskałeś dostęp do tego repozytorium, załóż własnego brancha i upewnij się, że wprowadzone przez Ciebie zmiany są tylko na tym branchu.
W celu commitowania do mastera wystaw pullrequesta. 

# How to run

### Requirements
* Java 11 albo wyższa.
* Ponad dependencje z JMetala (pliki pom.xml) dodano również:
    * [Lomboka](https://projectlombok.org/) - do generacji boilerplate kodu. W przypadku błędu w Twoim IDE poszukaj wtyczki np. [taką](https://plugins.jetbrains.com/plugin/6317-lombok).
    * [JUnita](https://junit.org/) - do paru testów.
    * [XChart](https://knowm.org/open-source/xchart/) - do generowania wykresów. (Z lekką nutą [Swinga.](https://docs.oracle.com/javase/tutorial/uiswing/index.html)
    
![Video tutorial](https://www.youtube.com/watch?v=8qqtNbU7L7M&feature=youtu.be).

## Runnery

Celem każdego z tych runnerów jest uruchomienie ich jako punktu wejściowego programu.
Przykład komendy do odpalenia:
```
F:\Java\jdk-14\bin\java.exe -Dfile.encoding=UTF-8 
-classpath {TWOJ_CLASS_PATH} org.uma.jmetal.example.multiobjective.emas.JMetal5EMASVisualExperimentRunner
```
Gdzie {TWOJ_CLASS_PATH} powinien zawierać ścieżki do odpowiednich jarów (dependencji) oraz do skompilowanych plików (z innych modułów również).
Najprościej jednak z konsoli tego nie odpalać i pozwolić żeby IDE wygenerowało to za nas. (zwyczajnie dając run)

* [Visual Runner](jmetal-example/src/main/java/org/uma/jmetal/example/multiobjective/emas/JMetal5EMASVisualExperimentRunner.java) - z wizualizacją algorytmów na wykresach. Można tutaj 
uruchomić kilka algorytmów naraz. Domyślnie zbierze i uruchomi on WSZYSTKIE algorytmy załączone do listy algorytmów w [AlgorithmFactory](jmetal-algorithm/src/main/java/org/uma/jmetal/algorithm/multiobjective/lemas/Algorithms/AlgorithmFactory.java).
* [Log Runner](jmetal-example/src/main/java/org/uma/jmetal/example/multiobjective/emas/JMetal5EMASLogExperimentRunner.java) - Runner bez wizualizacji który wypiszę metryki JMetala na standardowe wyjście. Uruchomi RAZ tylko PIERWSZY algorytm z listy algorytmów w [AlgorithmFactory](jmetal-algorithm/src/main/java/org/uma/jmetal/algorithm/multiobjective/lemas/Algorithms/AlgorithmFactory.java).
* [Averaging Runner](jmetal-example/src/main/java/org/uma/jmetal/example/multiobjective/emas/JMetal5EMASAveragingRunner.java) - Runner bez wizualizacji który wypisze:
    - HV oraz HVR
    - IDG oraz IDG+ (razem z ratio i znormalizowanymi).
    - Ilość ewaluacji
    
    Dodatkowo zapiszę on również wszystkie wyniki do pliku .csv w utworzonym folderze "emas_results".
    Uruchomi on X razy każdy z podanych algorytmów z listy algorytmów z [AlgorithmFactory](jmetal-algorithm/src/main/java/org/uma/jmetal/algorithm/multiobjective/lemas/Algorithms/AlgorithmFactory.java) 
    gdzie X to zmienna konfiguracyjna znajdująca się w środku runnera. Na koniec wszystkie wyniki zostaną zebrane i uśrednione (średnia arytmetyczna przez X).
    
    
### Porównanie z NSGAII
Żeby dodać do powyższych runnerów NSGAII trzeba korzystać z brancha origin/MKasprzyk. Niestety takie uruchomienie wymagało wprowadzenia tony warningów do runnerów stąd zostało to na branchu developowym.
Dodatkowo porównać można jedynie za pomocą: Visual Runnera i Log Runnera. Averaging Runner obecnie NIE jest wspierany.
Możliwe konfiguracje NSGAII znajdują się również w [AlgorithmFactory](jmetal-algorithm/src/main/java/org/uma/jmetal/algorithm/multiobjective/lemas/Algorithms/AlgorithmFactory.java).


# Importowanie projektu

* kazdy mam tam swoje sciezki/sposoby ale najprosciej to chyba bezposrednio z IJ
    
    - Chcesz utworzyc projekt - Tak - Import project from external model - maven - Search for project recursively, Import Maven projects automatically
    - select profiles (doclinit-java8-disable) - Next - Next - Finish
     
	![](http://jagular.iisg.agh.edu.pl/~siwik/emas/1.png) 
	![](http://jagular.iisg.agh.edu.pl/~siwik/emas/2.png)
	![](http://jagular.iisg.agh.edu.pl/~siwik/emas/3.png)
	![](http://jagular.iisg.agh.edu.pl/~siwik/emas/4.png)
	![](http://jagular.iisg.agh.edu.pl/~siwik/emas/5.png)
	![](http://jagular.iisg.agh.edu.pl/~siwik/emas/6.png)
	![](http://jagular.iisg.agh.edu.pl/~siwik/emas/7.png)


# (Pierwsze) uruchomienie.....


* Jako pierwsze uruchomienie najprosciej uruchomic jeden z dostepnych w jmetalu predefiniowanych" eksperymentow badajacych wydajnosc algorytmu NSGAII w tym celu 
   * wedrujemy w projekcie jMetal do jmetal-exec - > src - > main - > java -> org.uma.jmetal experiment i otwieramy plik NSGAIIStudy2
   * przy probie uruchomienia maina dostaniesz wyjatek Exception in thread "main" org.uma.jmetal.util.JMetalException: Needed arguments: experimentBaseDirectory
	at org.uma.jmetal.experiment.NSGAIIStudy2.main(NSGAIIStudy2.java:63)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:147)
jak widac w lini 65 jako pierwszy argument wywolania czytamy experimentBaseDirectory
* jesli chcemy odpalac to w IJ to http://stackoverflow.com/questions/2066307/how-do-you-input-commandline-argument-in-intellij-idea  i podajemy katalog powyzej katalogu repo tj. ../../Results/INazwisko/Timestamp 
* po zakonczeniu obliczen w katalogu eksperymentow znajdziesz katalog z nazwa eksperymentu a w nim trzy podkatalogi "data", "latex", "R"
* zeby zobaczyc/ocenic wyniki eksperymentu idziemy do katalogu latex, kompilujemy pliki latexowe (np tak: for i in *.tex; do pdflatex $i;done)
i ogladamy wyniki w plikach pdf (w szczegolnosci NSGAIIStudy2.pdf
* nastepnie wedrujemy do katalogu R i "Rujemy" pliki zrodlowe np tak: for i in *.R; do Rscript $i;done (pakiet R sciagniesz stad: https://www.r-project.org/) i ogladamy wygenerowane wykresy i rankingi
* poza rankingami i metrykami finalnymi generowanymi przez jmetalowy eksperyment zadbaj o to aby gromadzic wyniki co "n" iteracji / co "m" ewaluacji , (przyklad wizualizowany na panelu ponizej). Przygotuj sobie skrypty
ktore po zakonczeniu eksperymentu wygeneruja wartosci poszczegolnych metryk (uwzglednianych przez ekspeyment jmetalowy) w kolejnym co n0tym i co m-tym kroku, dodatkowo warto pokazac w tych krokach posrednich:  
ilosc osobnikow w populacji, ilosc osobnikow niezdominowanych, ilosc osobnikow "nowych" (rodzacych sie), ilosc osobnikow umierajacych. Najlepiej oczywiscie w postaci "wsteg" (min, max, srednia w danym kroku z n uruchomien bo byc moze biezaca charakterystyka to "przypadek"

* Dostepna implementacje EMASA
	* Lepsza/gorsza - Znajduje sie w jmetal-algorithm\src\main\java\org\uma\jmetal\algorithm\multiobjective\lemas\
	* Runner do tego: jmetal-exec\src\main\java\org\uma\jmetal\runner\multiobjective\JMetal5EMASVisualExperimentRunner.java
	* Po odpaleniu powinienes zobaczyc panel podobny do ponizszego:
	![Panel\label{fig:zdt3}](http://jagular.iisg.agh.edu.pl/~siwik/emas/panel.png) 
	* Eksperyment do tego siedzi w jmetal-exec\src\main\java\org\uma\jmetal\experiment\LEMASStudy.java   


# DEPRACTED ~~Wczytywanie iteracji~~ 

~~* Pierwszym krokiem jest uruchomienie runnera: exec\src\main\java\org\uma\jmetal\runner\multiobjective\JMetal5EMASVisualExperimentRunner.java z odpowiednią flagą "-debug". Jej dodanie powoduje zapisywanie się iteracji.~~

    ![](https://i.imgur.com/eGcud1h.png)
    
~~* Uruchamiamy program i liczymy tyle iteracji ile potrzeba. Żeby zapisać iteracje, liczenie należy spauzować używając przycisku "Pause", a następnie wybrać odpowiednią
iteracje sliderem i nacisnąć "Save iterations to SER file".~~

    ![](https://i.imgur.com/IqBYmrj.png)
    - last iteration - to ostatnia obliczona iteracja.
    - current iteration - to iteracja która jest zaznaczona przez slidera (czyli ta która jest narysowana).
    
~~* Wybieramy ilość iteracji jaką chcemy zapisać liczoną od poprzednio wypisanej iteracji. Przeważnie wystarczy zapisanie jednej (Czyli w dół).~~

    ![](https://i.imgur.com/yaJgq7L.png)
    
~~* Czekamy na wiadomość potwierdzającą sukces zapisu iteracji. Jeśli się powiodło to iteracje zapisane zostały w podfolderze programu o nazwie "//generated_genotypes//".~~   

~~* Można w tym momencie już zamknąć obliczenia i uruchomić nowego runnera do wczytania "org.uma.jmetal.runner.multiobjective.JMetal5EMASLoadRunner". Nie wymaga już flagi -debug, choć jej dodanie pozwoli nam na ponowne zapisanie.~~

~~* Po uruchomieniu runnera pojawi nam się okno wyboru plików do wczytania. Katalogi w "generated_genotypes" są ustawione datami, żeby łatwiej je było odróżnić. Wybieramy interesujący nas folder i otwieramy go.~~

  ![](https://i.imgur.com/tYzEfLx.png)
  
  
~~* Następnie musimy wybrać pliki które będą wczytane. Potrzebne jest zawsze:~~
    
    - zapisana iteracja (np. Better_iteration_16.ser)
    - zapisana iteracje EMAS-a (np. Better_EMAS_iteration_16.ser)  (Te liczby muszą się zgadzać).
    
    Można też wybrać specjalny seed, choć w przypadku w którym się go nie wybierze, zostanie on pobrany automatycznie z folderu w którym się znajdujemy.
    - random seed Agenta (np. randomAgentSeed_Better)
    - random seed JMetala (np randomJMetalSeed_Better)
    
    (W tym przypadku better to nazwa algorytmu, w ten sposób można rozróżnić o który algorytm chodzi.)

  ![](https://i.imgur.com/6ubpkaY.png)
  
    Wybieramy wszystkie pliki naraz i clickamy "Open".
    Alternatywnie można też nacisnąć "Auto Choose" bez wybierania plików, przez co wybiorą się one w sposób automatyczny. Wówczas wybrana iteracja będzie zawsze ostatnia (najwyższa) z folderu w którym się znajdujemy.
    
~~* Czekamy chwile na załadowanie i jeśli wszystko poprawnie się wczytało to Runner powinien normalnie uruchomić algorytm. Sukces, wszystkie iteracje będą wyliczone dokładnie w ten sam sposób co w poprzednim uruchomieniu.~~
