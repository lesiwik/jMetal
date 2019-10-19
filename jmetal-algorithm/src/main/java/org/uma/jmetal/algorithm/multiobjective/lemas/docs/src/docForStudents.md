#Kilka zdan wprowadzenia do (pareto)polioptymalizacji

##Definicja problemu i pojęciologia

Zadanie polega na poszukiwaniu alternatyw ktore jednocześnie optymalizowałyby
wiele (sprzecznych) kryteriow.

Oczywiście pojawia sie problem oceny/porównywania alternatyw. (Póki)co badania oparte 
są na relacji dominacji. W największym skrócie $a_1$ jest lepsze od $a_2$ jeśli pod względem
wszystkich kryteriów jest niegorsza od $a_2$ a przynajmniej pod względem jednego kryterium
jest silnie lepsza. Formalnie:

Aby zachowac ogolnosc definicji relacji dominacji, tak aby
obejmowała ona zarowno zadania (i~kryteria) maksymalizacji jak
i~minimalizacji, w~ramach definicji tej relacji wprowadzany jest
(uogólniony) dwuargumentowy operator porównania:
$\vartriangleright$. Zgodnie z~tym operatorem zapis $y_1$
$\vartriangleright$ $y_2$ oznacza, iz wartosc $y_1$ jest wartością
lepszą niż wartość $y_2$ z~punktu widzenia bieżącego celu. A~zatem
w~przypadku, w~którym określone kryterium stanowi przedmiot
minimalizacji, operator $\vartriangleright$ oznacza relację
mniejszoťci -- $(<)$, zaś w~sytuacji w~której kryterium
stanowi przedmiot maksymalizacji operator $\vartriangleright$
oznacza relację większości -- $(>)$. Zapis $y_1 \ntriangleleft y_2$
oznacza, że wartość $y_1$ jest wartością niegorszą od wartości $y_2$
z~punktu widzenia bieżącego celu. 

\begin{definition}[Relacja słabej dominacji]
Rozwiązanie $\vec{x}^a$ słabo dominuje rozwiązanie $\vec{x}^b$
(rozwiązanie $\vec{x}^a$ jest rozwiązaniem lepszym od rozwiązania
$\vec{x}^b$ z~punktu widzenia optymalizacji w~sensie Pareto)
($\vec{x}^a~\succeq \vec{x}^b$) wtedy i~tylko wtedy, gdy:

\begin{displaymath}
  \vec{x}^a~\succeq \vec{x}^b\Leftrightarrow \left\{ \begin{array}{ll}
      f_j(\vec{x}^a)\ntriangleleft f_j(\vec{x}^b)~~~dla~~~j=1,2\ldots ,M\\
      \exists i\in \{1,2,\ldots ,M\}:~~f_i(\vec{x}^a)\vartriangleright f_i(\vec{x}^b)\\
          \end{array} \right.
\end{displaymath}
\label{def:wdomination}
\end{definition}


Rozwiązanie w~sensie Pareto (i w dalszych rozważaniach)
problemu optymalizacji wielokryterialnej $MOOP$ oznacza poszukiwanie
wszystkich niezdominowanych (w~rozumieniu słabej relacji dominacji)
alternatyw ze zbioru $\mathcal{D}$ -- zbiór takich alternatyw
określany jest mianem zbioru Pareto $\mathcal{P}$.

Gdyby było potrzebne grubsze wprowadzenie/podbudowa/pojęciologia -- do przejrzenia np tutaj
[Tutaj](https://books.google.pl/books?id=OSTn4GSy2uQC&printsec=frontcover&source=gbs_ge_summary_r&cad=0#v=onepage&q&f=false)

##Założenia

Zadaniem/celem nie jest wybór określonej alternatywy (decision making) \
a znalezienie wielu/wszystkich/wszystkich w zbiorze rozwiązań, co do których wiadomo,
że nie istnieją lepsze. Oczywiście, część z tych rozwiązań będą (w pewnym sensie) "faworyzować"
jedne z kryteriów, inne rozwiązania będą "faworyzować" inne -- ale nie stanowi to (póki co)
przedmiotu rozważań. 

Innymi słowy solwer/optymalizator traktujemy jako czarną skrzynkę przyjmującą wyłącznie definicje problemu
i szukającą zbioru Pareto nie biorąc pod uwagę żadnych preferencji użytkownika co do wagi kryteriów, jego preferencji etc.

Podstawowe założenie co do solwera to to że ma być agentowo-ewolucyjny (cokolwiek by to nie oznaczało).
W praktyce przyjęto, że jest/ma to być solwer osadzony na systemach typu EMAS, do którego wprowadzamy
operatory/usprawnienia pozwalające na efektywne rozwiązywanie problem(ów) polioptymalizacji.

Operatory/modyfikacje które przyjęto jako możliwe do stosowania/wprowadzenia mogą być wprowadzane
zarówno na poziomie operatorów ewolucyjnych/wariacyjnych jak i agentowych (interakcje/decyzje) tak 
długo jak (przynajmniej docelowo) będą one mogły być zrealizowane "agentowo" -- czyli bez globalnego zarządzania
populacją, bez przeglądu zupełnego etc, a jedynie w wyniku rozproszonych interakcji/wymiany informacji pomiędzy agentami.
Nawet jeśli--na etapie eksperymentowania--jakiś mechanizm (dla uproszczenia) zrealizowany zostanie poprzez (iteracyjny)
przegląd(zupełny) populacji to musi być widać, że da się to (tego typu) mechanizm  zrealizować 
w postaci agentowej   
 


#Punkt wyjścia -- Podstawowy EMAS do polioptymalizacji
\label{ssec:baseemas}
Zaprzegniecie podstawowego EMASa do polioptymalizacji wydaje sie trywialne i.e. zamieńmy porównanie agentów 
decydujace o wymianie pomiedzy nimi energii/zasobow na dominacje w sensie Pareto i powinno dzialac. 
Niestety nie dziala. W niniejszym dokumencie zebrano zidentyfikowane problemy/przyczyny takiego stanu. 
Sa to problemy otwarte nadajace sie imho do natychmiastowego researchu.


Poniżej zarysowano podstawowy emas do polioptymalizacji w ktorym jedyne co zrobiono w kontekscie 
polioptymalizacji to zastosowano dominacje w sensie pareto jako czynnik decydujacy podczas spotkan 
o przeplywie pomiedzy agentami energii. 


```java
for(allAliveAgents){
    while(agent is alive){
        if(agent is out of resources){
            die();
        }else{
            findMeetingPartner(); //W praktyce wybiera losowego
            compareSolutionWithMeetingPartner(); //w oparciu o pareto dominacje
            if(agent A1 dominuje spotkanego agenta A2){
               transferEnergy(FromA2,ToA1); //transferujemy porcje energii 
                                            // od zdominowanego do dominujacego
            }
           
           if(agent A1 jest zdominowany przez spotkanego agenta A2){
               transferEnergy(FromA1,ToA2); //transferujemy porcje energii 
                                            // od zdominowanego do dominujacego
           }
           if(agent posiada energii powyzej progu reprodukcji){
               findReproductionPartner(); //w praktyce wybiera losowego 
                                          // z energia powyżej progu reprodukcji
               reprodukuj();
               mutuj();
               transferEnergy(FromParent,ToOffspring); //transferuj porcje 
                                                        // energii do potomka
           }
        }
    }
}
```

#Zidentyfikowane problemy w stosowaniu bazowego emasa do polioptymalizacji

Zarysowany w \ref{ssec:baseemas} podstawowy emas (w ktorym jedyne co 
zmieniono to wprowadzon dominacje do (wzajemnego) porównywania alternatyw/osobników) algorytm nie dziala przynajmniej z trzech powodow.


##Stagnacje niejako z definicji
\label{ssec:stagnacja}

Zeby w populacji cokolwiek sie dzialo agenci musza zyskiwac badz tracic energie 
- tylko wowoczas moga reprodukcowac, umierac etc. 
Tymczasem wystarczy wyobrazic sobie sytuacje jak na rys \ref{fig:stagnacja1}. 


![Sytuacja poczatkowa\label{fig:stagnacja1}](figures/zrzut1.png){width=50%}


Zaden z osobnikow w populacji nie dominuje zadnego innego. 
Zatem podczs spotkan nie ma zadnego czynnika ktory powodowalby przeplyw energi. 
A zatem nikt nie zyskuje energi i nikt jej nie traci. A zatem nikt nie zyskuje prawa do reprodukcji
 nikt tez nie umiera/nie jest usuwany z populacji. 
 Efekt? Przez tysiac iteracji sprawdzonych zostalo raptem 8 rozwiazan 
 (por. rys. \ref{fig:stagnacja2} - z czego 6 to poczatkowa populacja). 


![](figures/zrzut2.png){width=50%}
![](figures/zrzut3.png){width=50%}

![Stagnacja\label{fig:stagnacja2}](figures/t.png)

Czyli przez 1000 iteracji algorytmnu w populacji pojawilo sie raptem 2 nowych agentow 
(a to i tak jest sytuacja laboratoryjna bo parametry ustawione zostaly tak ze aby zyskac 
prawo do reprodukcji wystarczy chocby raz zdominowac kogolowiek. 

Podobnie zeby byc wyeliminowanym wystarczy chocby raz zostac zdominowanym.


Oczywiscie jest to sytuacja przejaskrawiona w tym sensie ze mamy skrajnie mala populacje 
wiec prawdopodobienstwo zajscia opisanej sytuacji jest duze. Przy wiekszej/duzej populacji 
troche dluzej trwa stan w ktorym - rzadko bo rzadko - ale ktos kogos dominuje 
ale wczesniej czy pozniej i tak dochodzi do opisanej sytuacji (stagnacja i zatrzymanie procesu 
podazania w kierunku frontu w stosunkowo duzej odleglosci od niego).

Porzadane zatem zwiekszenie sily/efektywnosci czynnika decydujacego o przeplywie energii. 
Innymi slowy jesli $a_1$ dominuje $a_2$ lub odwrotnie to sprawa jasna przeplyw 
od a1 do a2 albo odwrtonie ale co jezeli podczas spotkania zaden zadnego nie dominuje?

* losowo wybrac "dawce" i transferowac od dawcy do biorcy? - tyle tylko ze 
one sa wzajemnie niezdominowane a zatem relatywnie oba sa dobre - 
dlaczego zatem ktoregos "karac"? Za chwile dojdziemy do bladzenia przypadkowego.



##Dyskryminacja "ogonow" przez relacje dominacji

Kolejny powod dla ktorego to nie dziala bo nie moze niejako z definicji jest nastepujacy.
Mimo ze populacja poczatkowo wygldala np jak na rys \ref{fig:dyskryminacja}a czyli calkiem 
sensownie to stosunkowo szybko dochodzimy do sytuacji jak na rys \ref{fig:dyskryminacja}b
czyli ciazenie populacji w strone "poczatku ukladu wspolrzednych" 
(akurat tak to sie uklada przy definicji problemu ZDT1 ale w kazdym razie wycinane sa "ogony populacji").

![](figures/zrzut4.png){width=50%}
![](figures/zrzut5.png){width=50%}

![Dyskryminacja ogonow\label{fig:dyskryminacja}](figures/t.png)




Dlaczego tak sie dzieje. Wyjasnienie jest bardzo proste.

Wezmy przykladowego agenta z "prawego" ogona populacji zaznaczony na czerwono 
na rys \ref{fig:dyskryminacja3}. Zgodnie z relacja dominacji obszar w ktorym on dominuje 
inne rozwiazania to zaznaczona na czerwono "cwiartka" - w zasadzie zbior pusty. 
Jednoczesnie obszar dominacji dla agenta zlokalizowanego w "centrum" populacji 
to cwiartka "niebieska" - jak widac ma on spora (a rzedy wielkosci wieksza w stosunku 
do agentow z ogonow) szanse na spotkania kogos kogo dominuje, 
a wiec na zyskanie energi, zyskanie prawa reprodukcji, reprodukcje etc - 
w konsekwencji stopniowe wymieranie "ogonow" i namnazanie agentow "w centrum" - 
efekt skrajnie nieporzadany zarowno co do zasady w ewolucji jak i tym bardziej 
w polioptymalizacji bo naszym zadaniem jest odkrycie frontu na calej jego rozciaglosci
(innymi slowy znalezienie calego zbioru zielonego). 
Aktualnie nie ma na to szans "z definicji". Juz o sytuacjacch z trudniejszymi frontami 
(nieciaglymi, wkleslymi etc) niewspominajac.

![Dyskryminacja\label{fig:dyskryminacja3}](figures/zrzut6.png){width=50%}

##Gorszy nie znaczy zdominowany, lepszy nie znaczy dominujacy

Trzeci ze zidentyfikowanych problemow jest w zasadzie konsekwencja omowionego wczesniej. 
Otoz wezmy dwoch agentow czerwonego i niebieskiego zaznaczonego 
na rys \ref{fig:lepszygorszy1}. 
Na oko a1 jest lepszy od a2 (jest blizej frontu, nie jest przez nikogo zdominowany) 
ale przy bezposrednim spotkaniu a1 z a2 a1 nie zyska ani grama energii/zasobow 
bo zgodnie z definicja paerto dominacji a1 i a2 sa wzajemnie niezdominowane. Potrzebny zatem mechanizm ktory by to adresowal.

![Lepszygorszy\label{fig:lepszygorszy1}](figures/zrzut7.png){width=50%}

##Brak mechnizmow odpowiedzialnych za "rozproszenie" rozwiazan po calym froncie

Aktualnie brak jest mechanizmu ktory pozwalalby obsadzac "cala rozciaglosc" frontu.
Potrzebny bylby mechanizm ktory albo pozwalalby "schodzic" populacji do frontu na calej 
jego rozciaglosci (por rys \ref{fig:rozciaglosc1}a)

![](figures/zrzut8.png){width=50%}
![](figures/zrzut9.png){width=50%}

![Rozciaglosc\label{fig:rozciaglosc1}](figures/t.png)

albo taki ktory pozwalalby (agresywnie) "schodzic" w strone frontu a pozniej te rozwiaznia 
"rozpychac" po calej szerokosci frontu \ref{fig:rozciaglosc1}b).


W mojej ocenie podejscie drugie (czyli jakies niszowanie, crowding etc) to raczej lata 
na problem niz zaadresowanie samego problemu. Oczywiscie tego typu mechanizm mozna dodac 
ale przede wszystkim nalezaloby spowodowac zeby tego typu zjawisko koncentracji w ogole 
nie zachodzilo badz bylo "niszowe" i zebysmy pochodzili do frontu na (mozliwie) calej jego rozciaglosci


Wspomniane powyzej problemy zdecydowanie nabrzmiewaja 
wraz ze zwiekszaniem trudnosci problemu. O ile bowiem w przypadku problemu
ciaglego wkleslego mozna sobie wyobrazic wprowadzenie mechanizmow ktore 
rozwiazanie skupione wokol poczatku ukladu wspolrzednych "rozpychaly" wzdluz calego frontu
o tyle dla problemu wypuklego (zwlaszcza z siodlami) jakiekolwiek ruszenie 
sie na prawo od poczatku przy probie "rozpychania" powoduje ze te rozwiazanie "na prawo" sa zdominowane
przez te blisko poczatku i jesli dominacja jest zasadniczym czynnikiem selekcji/nagradzania/karania
osobnikow to alternatywy "na prawo" zostana (niemal natychmiast) "zjedzone" przez te 
zlokalizowane blisko poczatku ukladu wspolrzednych.

![zdt3\label{fig:zdt3}](figures/zdt3.PNG)

Z kolei przy problemie z frontem nieciaglym jak na rys \ref{fig:zdt3} w ogole nie ma szans na obsadzenie duzych kawalkow frontu...

##Nie (tylko) dominacja 
W zasadzie wwszystkie zidentyfikowane problemy, wynikaja z polaczenia rozproszonej/lokalnej natury EMASA
z wlasciwosciami dominacji. W pierwszej kolejnosci nalezaloby sie zatem temu wlasnie przyjrzec.  

###AreaOFControlDominance

Generalna idea nastepujaca:

![areaUnderControl-Idea1\label{fig:areaidea1}](figures/areaIdea1.png){width=100%}

Jesli emas korzysta z "klasycznej" Pareto dominacji to jezeli w sytuacji jak na rys
\ref{fig:areaidea1} spotkaja sie $A_1$ i $A_3$ to mimo, ze "na oko" widac 
ze $A_1$ jest lepszy (jest blizej frontu, nikt go niedominuje etc) niz $A_3$ to nic sie 
nie stanie bo ani $A_3$ nie lezy w obszarze zdominowania $A_1$, ani $A_1$ nie lezy 
w obszarze zdominowania $A_3$ (obszary zaznaczone na czerwono). 

![areaUnderControl-Idea2\label{fig:areaidea2}](figures/areaIdea3.png){width=100%}

Ale wyobrazmy sobie ze zanim dojdzie do spotkania agentow $A_1$ i $A_3$
wczesniej $A_1$ spotka sie z agentem $A_2$. $A_1$ i $A_2$ są wzajemnie niezodminowane
i mozna uznac ze $A_1$ jest lepszy zarowno od wszytskich agentow zlokalizowanych 
w jego obszarze zdominowania jak i od tych zlokalizowanych w obszarze zdominowania agenta $A_2$
A zatem budujemy dla obu agentow ($A_1$ i $A_2$)  "obszar kontroli" stanowiacy sume obszarow 
zdominowania obu agentow.
 
Od teraz zatem obszar kontroli obu tych agentow to obszar zaznaczony na pomaranczowo na rys 
\ref{fig:areaidea2}. 

Jesli teraz dojdzie do spotkania agentow $A_1$ i $A_3$ to mimo ze $A_3$ nie lezy w obszarze zdominownaia 
Agenta $A_1$ (zaznaczony na czerwowno na rys \ref{fig:areaidea1}) to znajduje sie on
w jego obszarze kontroli (pomaranczowy na rys \ref{fig:areaidea2}) (suma obszarow zdominowania spotkanych dotychczas
agentow z ktorymi $A_1$ byl wzajemnie niezdominowany). W konsekwencji, w mysl obszaru kontroli
$A_1$ okaze sie lepszy od $A_3$, przejmie (czesc) jego energi etc.

####Weryfikacja empiryczna 

![AreaUnderControl\label{fig:area0}](figures/areaUnderControl0.PNG){width=100%}

![AreaUnderControl\label{fig:area2}](figures/areaUnderControl2.PNG){width=100%}

![AreaUnderControl\label{fig:area1}](figures/areaUnderControl.PNG){width=100%}


- jak widac na rys \ref{fig:area0} dla ZDT1 front dostaje sie bardzo ladny (moglby "zejsc" jeszcze troche nizej) ale 
nie ma stagnacji, nie ma tego ciazenia do lewej strony.
- na pozostalych dwoch rysunkach tj \ref{fig:area2} i \ref{fig:area1} porównanie tego mechanizmu w kilku wariantach
i puszczenie z ciekawosi dla ZDT3 - jak widac na pewno do dalszego dopracowywania bo momentami zachowuje sie "dziwnie" ale na pewno jest w tym potencjal

####Pytania do odpowiedzi/kwestie do sprawdzenia
![AreaUnderControlZDT2\label{fig:areaZDT2}](figures/areaUnderControlZDT3.PNG){width=100%}

- Dlaczego dla problemu wkleslego typu ZDT2 powyzszy mechanizm nadal radzi sobie slabo. Troche to wyglada tak jakby dzialanie tego wstepnego warunku na dominacje byl tak silny ze 
sciaga populacje w lewo ale do weryfikacji co sie tam tak naprawde dzieje

![AreaAngleIdea1\label{fig:areaangleidea}](figures/areaangleIdea1.png){width=100%}

- Sprawdzic czy mechanizm w ktorym obszar kontroli to nie suma obszarow zdominowania ale
rozszerzenie "o odpowiedni kat" (idea jak na rys \ref{fig:areaangleidea}) dawaloby istotna poprawe (\textbf{uwaga na wkleslosci frontu/obszaru})
 
\newpage

###Extended Angle Dominance

#### Przeglad literatury wraz z analiza i omowieniem
https://link.springer.com/content/pdf/10.1007/978-3-662-46578-3_79.pdf

https://link.springer.com/content/pdf/10.1007%2F978-3-662-46578-3.pdf  (strona 673 i dalsze)

https://books.google.pl/books?id=1SlBDgAAQBAJ&pg=PA95&lpg=PA95&dq=zdt2+problem+property&source=bl&ots=ONivV1mwQW&sig=qR1X6j2ZI20FotzmtOUNPk9vhy4&hl=en&sa=X&ved=0ahUKEwjL3OH0lJHZAhWGhiwKHZ1SA_gQ6AEIKTAA#v=onepage&q=zdt2%20problem%20property&f=false (strona 92 i dalsze)

#### Koncepcja realizacji

#### Analiza dzialania na poziomie koncepcyjnym

#### Analiza dzialania na poziomie realizacyjnym

#### Weryfikacja empiryczna
 
\newpage


###$\alpha$-dominance
#### Przeglad literatury wraz z analiza i omowieniem

#### Koncepcja realizacji

#### Analiza dzialania na poziomie koncepcyjnym

#### Analiza dzialania na poziomie realizacyjnym

#### Weryfikacja empiryczna

\newpage

###$\epsilon$-dominance
#### Przeglad literatury wraz z analiza i omowieniem
#### Koncepcja realizacji

#### Analiza dzialania na poziomie koncepcyjnym

#### Analiza dzialania na poziomie realizacyjnym

#### Weryfikacja empiryczna


\newpage

###L-dominance
#### Przeglad literatury wraz z analiza i omowieniem

#### Koncepcja realizacji

#### Analiza dzialania na poziomie koncepcyjnym

#### Analiza dzialania na poziomie realizacyjnym

#### Weryfikacja empiryczna
 
