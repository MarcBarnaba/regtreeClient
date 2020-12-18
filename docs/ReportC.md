# Relazione Tecnica Finale

## Indice
1. Introduzione
2. Guida Installazione
3. Diagrammi delle classi
4. Riepilogo del test    
5. Manuale utente



## 1. Introduzione
Lo scopo di questo documento è illustrare le scelte compiute durante lo sviluppo del software e di elencare le funzioni dello stesso.

Il client è un applicativo Java che consente di effettuare previsioni usufruendo del servizio di predizione remoto.

Il server include funzionalità di data mining per l’apprendimento di alberi di regressione e uso degli stessi come strumento di previsione.

## 2. Guida Installazione

Il progetto è disponibile, sia lato client che lato server, su Github.
E' possibile clonare i due repository situati ai link:

Client : https://github.com/MarcBarnaba/regtreeClient.git 

Server : https://github.com/MarcBarnaba/regtreeServer.git

Entrambi utilizzano Maven per la gestione della build e delle dipendenze necessarie al funzionamento del progetto. Una volta clonati i progetti e importati in Eclipse, sarà sufficiente creare una Run Configuration per eseguire il progetto. In particolare:

> Run -> Run As -> Maven Build -> New launch configuration 

Dopo aver assegnato un nome alla configurazione, modificare il campo Goals inserendo

    clean javafx:run
e poi eseguire l'applicazione 
> Run -> Run As -> Maven Build -> nome_config -> Run

Assicurandosi che la variabile d'ambiente JAVA_HOME punti alla posizione del JDK usato. Ad esempio:

    JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.1.jdk/Contents/Home

### Troubleshooting

Utilizzando Maven per compilare ed eseguire il progetto, non dovrebbe essere necessario, ma è possibile che sia necessario aggiungere al progetto il JavaFX JDK:
> File -> Properties -> Java Build Path -> Libraries

Il progetto utilizza la versione 14.0.1 di JavaFX, come è possibile notare dai file pom.xml. Esso fa parte dei file consegnati insieme al codice.

Per ulteriori informazioni, è possibile consultare [questa](https://openjfx.io/openjfx-docs/) pagina.

## 3. Diagramma delle classi

 ![it.map1920.regtreeClient](/docs/uml/default.png "it.map1920.regtreeClient")

 Il package it.map1920.regtreeClient contiene:
 - Settings, una classe per conservare e gestire le preferenze dell'utente e ricordare le tabelle recenti
 - Connection, una classe per gestire lo scambio di messaggi con il server durante le fasi di connessione, learning e prediction.

![it.map1920.regtreeClient.controller](/docs/uml/controller.png "it.map1920.regtreeClient.controller")

Il package it.map1920.regtreeClient.controller contiene una classe Controller per ogni View presente nell'applicazione. I Controller utilizzano fxid delle componenti fxml per gestirle e impostano le azioni da eseguire in relazione ad azioni effettuate dall'utente.

![it.map1920.regtreeClient.ui](/docs/uml/ui.png "it.map1920.regtreeClient.ui")

La classe MenuActions gestisce la barre dei Menu e la inizializza sempre uguale in ogni vista. 
La classe ViewLoader, invece, offre metodi per sollevare degli Alert e per cambiare la Scene nello Stage dell'applicazione.





## 4. Riepilogo del test 

Se si inserisce un nome di una tabella non valido, cioè vuoto o inesistente, il sistema visualizza un alert

![Tabella Inesistente](/docs/screenshots/testcases/tabella_inesistente.png "Tabella Inesistente")

Se, nella fase di previsione, si tenta di andare avanti senza aver selezionato un'opzione, il sistema visualizza un alert

![No option selected](/docs/screenshots/testcases/no_option.png "No option selected")

Se, nella schermata delle impostazioni, si inserisce un numero di porta non numerico, il sistema visualizza un alert

![PortNumber Error](/docs/screenshots/testcases/portnumber_error.png "PortNumber Error")

## 5. Manuale utente

### Server

Per far funzionare il server, è sufficiente avviare l'applicazione regtreeServer.
La schermata dell'applicazione visualizzerà un form dove è possibile specificare il numero di porta che il Server utilizzerà per accettare nuove connessioni. Non inserendo alcuna porta il Server verrà avviato sulla porta di default, 8080.

![Server Home](/docs/screenshots/userguide/Server.png "Server Home")


![Server Avviato](/docs/screenshots/userguide/Avviato.png "Server Avviato")

### Client

Se l'applicazione viene avviata per la prima volta, verrà visualizzata una schermata di benvenuto.

![Client Welcome](/docs/screenshots/userguide/Welcome.png "Client Welcome")

Premendo il pulsante, viene visualizzata la Home, dove è possibile scegliere la fonte da cui attingere ai dati (Database o Archivio interno) e inserire il nome della tabella che contiene il dataset che si vuole utilizzare.

![Client Home](/docs/screenshots/userguide/Home.png "Client Home")


A questo punto, dopo aver premuto il pulsante "Learn", inizia la fase di previsione.
L'utente può scegliere il ramo dell'albero da percorrere, fino al nodo foglia che rappresenta il classvalue calcolato per la regola specificata attraverso la scelta delle singole opzioni.

![Pred Phase](/docs/screenshots/userguide/PredPhase.png "Pred Phase")

Terminata la fase di previsione, viene visualizzato il classvalue. I due bottoni presenti permettono di:
- eseguire una nuova previsione basata sullo stesso dataset
- scegliere un altro dataset per effettuare una previsione

![Classvalue](/docs/screenshots/userguide/ClassValue.png "Classvalue")


Dal menu File è possibile eseguire diverse azioni:
- Aprire una nuova finestra
- Aprire una tabella utilizzata di recente
- Chiudere il programma

![Menu File](/docs/screenshots/userguide/MenuBar.png "Menu File")


Dal menu Help, è possibile:
- Consultare la guida utente
- Aprire le Impostazioni

Le impostazioni permettono di scegliere se visualizzare la schermata di benvenuto all'avvio e di specificare valori diversi da quelli di default per la connessione al server.

![Preferences](/docs/screenshots/userguide/Settings.png "Preferences")




