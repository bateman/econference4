**Indice**


## Come estendere eConference 4 ##
Al contrario della versione precendente, eConference 4 presenta una struttura modulare, molto organizzata, e non permette che un servizio possa accedere alle funzionalità specifiche presenti nell'implementazione di un determinato protocollo.

![http://econference4.googlecode.com/svn/wiki/img/extend1.jpg](http://econference4.googlecode.com/svn/wiki/img/extend1.jpg) <br />
**Figura 1. Interazione tra i plugin in eConference 3.**

Come si può notare dalla Figura 1, in eConference 3, invece, il plugin Jabber (implementazione del protocollo XMPP) ha delle dipendenze con il servizio Chat. Inoltre, il servizio MultiChat ha delle dipendenze con Jabber ed addirittura con Smack (libreria usata per il supporto ad XMPP) che dovrebbe essere visibile solo a Jabber.

La struttura attuale, come si evince dalla Figura 2, è stata sviluppata tenendo conto dei seguenti accorgimenti:
  * Le librerie utilizzate per il supporto ai vari protocolli (Skype4Java, Smack..) possono essere utilizzate solo e unicamente dal plugin che implementa quel protocollo stesso (es. Skype, Jabber...).
  * Ogni servizio può creare delle dipendenze solo con un altro servizio e con il plugin Network , il quale fornisce una serie di strumenti che tutti i protocolli mettono a disposizione (eventi di rete, invio e recezione di messaggi, chiamate VoIP..).
  * I vari protocolli devono essere sviluppati senza tener conto dei servizi presenti nel sistema ma semplicemente fornendo un'implementazione all'interfaccia IBackend e tutte le interfacce ad essa collegate.

![http://econference4.googlecode.com/svn/wiki/img/extend2.jpg](http://econference4.googlecode.com/svn/wiki/img/extend2.jpg) <br />
**Figura 2. Interazione tra i plugin in eConference 4**

Nella versione 4 è stata introdotta un'ulteriore novità: i plugin Chat e MultiChat sono stati rimpiazzati da one2one e m2m. Questa decisione è stata presa in virtù del fatto che ora al supporto testuale è stato affiancato quello del VoIP e quindi è di nostro interesse distinguere solamente se la comunicazione avviene tra due o un gruppo di utenti.

### Struttura dei package ###
Le classi facenti parte del core di eConference si trovano nei package _it.uniba.di.cdg.xcore.`*`_. Tutte le classi esterne al core di eConference, per es. it.uniba.di.cdg.skype, it.uniba.di.cdg.jabber, org.apertium.api.translate, it.uniba.di.cdg.econference.planningpoker possono riferirsi alle risorse del core, cioé _importare_ classi _core_, ma non il viceversa.

## Meccanismi di astrazione ##
Per creare un livello di astrazione solido è stato necessario usare dei particolari meccanismi. L'idea che sta alla base di questa soluzione è che tutti protocolli hanno in comune due cose:
  1. Permettono di eseguire delle azioni (es. inviare un messaggio, inoltrare una chiamata, comunicare in cambiamento del proprio nickname..)
  1. Intercettano degli eventi di rete (es. un messaggio ricevuto, una chiamata in entrata, il cambiamento del roster)
In virtù di questo sono state definite tutte le azioni di base che un protocollo dovrebbe mettere a disposizione e tutti gli eventi che un protocollo dovrebbe intercettare.


### Action ###
Nella Figura 3 sono mostrate che quattro classi che forniscono le Action.

![http://econference4.googlecode.com/svn/wiki/img/extend3.jpg](http://econference4.googlecode.com/svn/wiki/img/extend3.jpg) <br />
**Figura 3. Implementazione delle action con il protocollo Skype**

Le Action sono state suddivise in quattro interfacce:
  * ICallAction
  * IMultiCallAction
  * IChatServiceActions
  * IMultiChatServiceActions

Questa divisione è stata fatta perché si può presentare il caso in cui un protocollo offra supporto a una sola categoria di Action. Per esempio Smack non fornisce supporto al VoIP, MSN non fornisce supporto al VoIP di gruppo e così via.

In Figura 3 sono mostrate le implementazioni a queste quattro interfacce. E' possibile avere accesso alle istanze di queste quattro classi solo tramite l'interfaccia IBackend che ci restituirà l'implementazione fornita dal protocollo utilizzato in quello specifico momento.

In Figura 4 è mostrato come i vari servizi abbiano accesso alle action. Con questo sistema si ha un'astrazione dal protocollo solida e infatti è possibile aggiungere in qualsiasi momento un nuovo servizio in quanto esso utilizzerà solo le Action generiche e non quelle specifiche di un protocollo.

![http://econference4.googlecode.com/svn/wiki/img/extend4.jpg](http://econference4.googlecode.com/svn/wiki/img/extend4.jpg) <br />
**Figura 4. Uso delle Action da parte dei servizi**

E' inoltre possibile aggiungere nuove Action (es. il supporto alla videochiamata 1-a-1 e/o di gruppo).

### Event ###
Sono stati i individuati gli eventi comuni tra i vari protocolli ed è stato creato uno strato di astrazione che li supportasse. Come si può notare dalla Figura 5, i protocolli (in questo caso, Skype) sollevano un evento e i servizi (in questo caso, one2one) lo intercettano e lo gestiscono in modo opportuno.

![http://econference4.googlecode.com/svn/wiki/img/extend5.jpg](http://econference4.googlecode.com/svn/wiki/img/extend5.jpg) <br />
**Figura 5. Astrazione degli eventi**

In Figura 6 è mostrato come funziona concretamente il sistema ad eventi. Il servizio si registra un proprio IBackendListener tramite il metodo registerBackendListener() esposto da INetworkBackendHelper e in seguito il plugin che implementa il protocollo solleva i vari eventi richiamando il metodo notifyBackendEvent() esposto sempre da INetworkBackendHelper.

![http://econference4.googlecode.com/svn/wiki/img/extend6.jpg](http://econference4.googlecode.com/svn/wiki/img/extend6.jpg) <br />
**Figura 6. Esempio di uso degli eventi**

### Extension ###
Dopo aver creato lo strato di astrazione, si è presentato un grosso ostacolo. I servizi di MultiChat strutturate comunicano tra loro tramite un particolare protocollo che estende il protocollo di base. Ad esempio il servizio eConference ha la necessità di comunicare a tutti i partecipanti quando la conferenza ha inizio e quando ha fine.
Si può ben immaginare che nessun protocollo metta a disposizione tali funzionalità. La soluzione adottata è stata quella di creare un meccanismo per estendere il protocollo di base. Ciò è permesso tramite:
  * una Action chiamata !sendExtensionProtocolMessage(), che invia un messaggio speciale che può contenere informazioni formattate in qualsiasi modo.
  * un Event chiamato ChatExtensionProtocolEvent, che viene sollevato quando si riceve un messaggio speciale.

Tramite questo meccanismo è possibile ridefinire infiniti protocolli su quello di base sviluppati ad hoc per ogni singolo servizio.

## Come inserire un nuovo protocollo di comunicazione ##
Per inserire un nuovo protocollo bisogna creare un nuovo plugin, estendere il plugin Network tramite l'interfaccia IBackend e fornire l'implementazione di alcune interfacce. Vediamo ora come eseguire queste operazioni.
Come primo passo scarichiamo il codice sorgente di eConference 4 in Eclipse tramite SVN dall'indirizzo (come mostrato in Figura 7).

**N.B. per maggiori dettagli su come compilare ed eseguire eConference 4, consultate la seguente [pagina](howtobuild.md) wiki.**

![http://econference4.googlecode.com/svn/wiki/img/extend7.png](http://econference4.googlecode.com/svn/wiki/img/extend7.png) <br />
**Figura 7. Checkout di eConference 4.0**

Come secondo passo creiamo un nuovo plugin da "File > New > Other" e selezioniamo "Plug-In Project" come mostrato in Figura 8.

![http://econference4.googlecode.com/svn/wiki/img/extend8.png](http://econference4.googlecode.com/svn/wiki/img/extend8.png) <br />
**Figura 8. Creazione di un nuovo plugin**

A questo punto apriamo il file "plugin.xml" e dal pannello extension clicchiamo su "add", nel wizard che compare selezioniamo l'estensione di nome "it.uniba.di.cdg.xcore.network.backends". Se tutto è andato a buon fine, dovrebbe comparire un pannello simile a quello in Figura 9.

![http://econference4.googlecode.com/svn/wiki/img/extend9.png](http://econference4.googlecode.com/svn/wiki/img/extend9.png) <br />
**Figura 9. Punto di estensione di IBackend**

Possiamo notare come in Figura 9 sia associata all'estensione la classe "SkypeBackend", quindi se stiamo implementando il protocollo X assoceremo all'estensione una classe chiamata XBackend.
Ora non ci rimane che realizzare tutti i metodi esposti nell'interfaccia IBackend con la nostra classe XBackend elencati in Figura 10. Bisogna, naturalmente, fornire anche un'implementazione per tutte le interfacce correlate ad IBackend (dove possibile):
  * IBuddyRoster
  * IChatServiceActions
  * IMultiChatServiceActions
  * ICallAction
  * IMultiCallAction

![http://econference4.googlecode.com/svn/wiki/img/extend10.png](http://econference4.googlecode.com/svn/wiki/img/extend10.png) <br />
**Figura 10. Metodi di IBackend da implementare**

## Stralci di codice del plugin per Skype ##
Per comprendere meglio il corretto modo di implementare un nuovo protocollo analizziamo in seguito alcuni metodi implementati per il protocollo Skype.
Diamo uno sguardo al metodo IBackend.Connect().

```
@Override
	public void connect(ServerContext ctx, UserContext userAccount)
			throws BackendException {
		Connector.Status status = null;
		Connector conn = Connector.getInstance();
		try {
			status = conn.connect();
		} catch (ConnectorException e1) {
			e1.printStackTrace();
			throw new BackendException(e1.getMessage());
		}
		if(status != Connector.Status.ATTACHED)
			throw new BackendException(new Exception("You have to install and run Skype before running eConference.\n
Please download  Skype from www.skype.com"));
		//notifico l'avvenuta connessione
		helper.notifyBackendEvent(new BackendStatusChangeEvent( ID, true ));
		//notifico l'aggiornamento del roster
		skypeBuddyRoster.updateRoster();
		//aggiungo i listeners di Skype4Java
		try {
			Skype.addChatMessageListener(chatMessageListener);
			Skype.addCallListener(callListener);
		} catch (SkypeException e) {
			e.printStackTrace();
		}
		connected = true;
	}
```

Il metodo appena presentato ha la funzione di effettuare una connessione. Analizziamo in dettaglio cosa fa.
La chiamata al metodo connect() di Connector restituisce lo stato di connessione di Skype. Lo stato cui ci riferiamo è quello del client Skype che deve essere avviato e connesso correttamente. Se lo stato è diverso da "ATTACHED" viene sollevata un'eccezione, in alternativa si procede. In seguito viene aggiornato il roster, vengono registrati i listener nella libreria skype4java e viene settata a true la variabile connected che mantiene l'informazione sullo stato della connessione.
Diamo uno sguardo al metodo IChatAction.!sendExtensionProtocolMessage().

```
@Override
public void sendExtensionProtocolMessage(String to, String extensionName,HashMap<String, String> param) {
 param.put(ExtensionConstants.CHAT_TYPE,
     ExtensionConstants.ONE_TO_ONE);
 String message = XmlUtil.writeXmlExtension(extensionName, param);
 try {
  Skype.chat(to).send(message);
 } catch (SkypeException e) {
  e.printStackTrace();
 }
}
```

Il metodo appena presentato si occupa dell'invio di un'estensione al protocollo. Viene aggiunto in parametro "CHAT\_TYPE" con valore "ONE\_TO\_ONE" per denotare che il messaggio che si sta inviando è un'estensione per la chat uno ad uno. Poi vengono trasformati in una stringa XML il nome dell'estensione e l'elenco dei parametri. Quindi, è inviata la stringa generata al destinatario.

Di seguito è mostrata la gestione dei messaggi in ingresso ed in particolare quella di un messaggio che estende il protocollo presente nel metodo processMessageReceived di SkypeBackend:

```
if(XmlUtil.chatType(content).equals(ExtensionConstants.ONE_TO_ONE))
 {
 if(extensionName.equals(ExtensionConstants.CHAT_MESSAGE)){
  HashMap<String, String> param = XmlUtil.readXmlExtension(content);
  String msg = param.get(ExtensionConstants.MESSAGE);
  IBackendEvent event = new ChatMessageReceivedEvent(
    senderId, msg, getBackendId());
  getHelper().notifyBackendEvent(event);
}
```

Viene controllato se si tratta di un messaggio di chat uno ad uno ed in particolare di un'estensione, se si verificano entrambe le condizioni si solleva un evento di ChatMessageReceivedEvent. Tale evento sarà intercettato e gestito da uno o più servizi come one2one, m2m, eConerence o one2oneinfo (che creeremo nel prossimo paragrafo).

## Come creare un nuovo servizio in eConference 4 ##
Per creare un nuovo servizio bisogna prima di tutto creare un nuovo plugin (come spiegato nel paragrafo 4.2). In seguito è possibile aggiungere nuovi elementi grafici tramite i meccanismi di estensione messi a disposizione da Eclipse. In Figura 11 abbiamo un esempio di una tale estensione, si può notare la View "Card Deck" che è stata aggiunta al modello di base di MultiChat strutturata.
Per quanto riguarda l'aspetto della comunicazione, è possibile accedere ai vari meccanismi messi a disposizione tramite un'istanza di IBackend. Possiamo avere accesso a tale istanza tramite la seguente chiamata:
NetworkPlugin.getDefault().getRegistry().getDefaultBackend();

![http://econference4.googlecode.com/svn/wiki/img/extend11.png](http://econference4.googlecode.com/svn/wiki/img/extend11.png) <br />
**Figura 11. eConference planning poker plugin**

### Creazione del nuovo servizio ###
Creiamo ora un piccolo servizio che estende eConference 4. Tramite tale servizio ogni utente avrà la possibilità di avere informazioni sul sistema usato da un altro utente. Faremo in modo che cliccando col pulsante destro del mouse su un utente presente nel roster compaia una voce chiamata "Get Info" e selezionando tale voce riceveremo informaizioni su quell'utente.
Come primo passo creiamo un nuovo plugin Project selezionando "File > New > Other", poi "Plug-in Project" e poi chiamiamo il progetto "it.uniba.di.cdg.xcore.one2oneinfo" e il suo Activator _it.uniba.di.cdg.xcore.one2oneinfo.One2OneInfoPlugin_. Infine clicchiamo sulla voce "activator" per creare la classe referenziata.
Apriamo il file "plugin.xml" creato in automatico all'interno del progetto ed entriamo nel pannello Extension. Clicchiamo su "add" e selezioniamo "popupMenu" come mostrato in Figura 12.

![http://econference4.googlecode.com/svn/wiki/img/extend12.png](http://econference4.googlecode.com/svn/wiki/img/extend12.png) <br />
**Figura 12. Aggiunta di un estensione ad un popupMenu**

Clicchiamo col pulsante destro del mouse su _org.eclipse.ui.popupMenu_ e poi su "New > objectContribution". Riempiamo in campo objectClass con _it.uniba.di.cdg.xcore.network.model.IBuddy_ che rappresenta l'utente all'interno del roster.
Clicchiamo ora col pulsate destro del mouse sull' objectContribution appena creato e selezioniamo "New > Action". Inseriamo in label "Get Info" e su class _it.uniba.di.cdg.xcore.one2oneinfo.GetUserInfo_. Infine clicchiamo sulla voce "class" per creare la classe. Se tutto è andato a buon fine avremo un pannello simile a quello in Figura 13.

![http://econference4.googlecode.com/svn/wiki/img/extend13.png](http://econference4.googlecode.com/svn/wiki/img/extend13.png) <br />
**Figura 13. Estensione del popupMenu**

Adesso estendiamo, con lo stesso procedimento, il plugin _org.eclipse.ui.startup_, aggiungiamo una voce "startup" e nel campo class inseriamo _it.uniba.di.cdg.xcore.one2oneinfo.One2OneInfoPlugin_. Infine facciamo in modo che la classe _One2OneInfoPlugin_ implementi l'interfaccia _IStartup_.
Con quest'ultima operazione abbiamo fatto in modo che il plugin venga avviato insieme ad eConference e non solo quado richiamata, questo serve ad avviare il demone di cui parleremo dopo.
All'interno di _One2OneInfoPlugin_ inseriamo il seguente codice:

```
/**
 * The activator class controls the plug-in life cycle
 */
public class One2OneInfoPlugin extends AbstractUIPlugin implements IStartup {
	// The plug-in ID
	public static final String PLUGIN_ID = "it.uniba.di.cdg.xcore.one2oneinfo";
	// The shared instance
	private static One2OneInfoPlugin plugin;
	/**
	 * The constructor
	 */
	public One2OneInfoPlugin() {
	}
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("One2OneInfoPlugin start..");
		plugin = this;
		new InfoDaemon ().start();
	}
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static One2OneInfoPlugin getDefault() {
		return plugin;
	}
	@Override
	public void earlyStartup() {
	}
}
```

Come si può notare all'avvio del plugin viene creata un'istanza della classe _InfoDaemon_  e viene chiamato il metodo _start()_. Quest'ultima classe si occuperà di gestire le richieste in entrata di informazioni sul nostro sistema e di inviare tali informazioni al richiedente.
Creiamo quindi una classe chiamata _InfoDaemon_  e riempiamola con il seguente codice:

```
public class InfoDaemon  implements IBackendEventListener {

 public void start(){
  for (IBackendDescriptor d : NetworkPlugin.getDefault()
		  .getRegistry().getDescriptors())
   NetworkPlugin.getDefault().getHelper().registerBackendListener(
		   d.getId(), this);
 }

 @Override
 public void onBackendEvent(IBackendEvent event) {
  if (event instanceof ChatExtensionProtocolEvent) {
   IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
   IChatServiceActions chat = b.getChatServiceAction();
   ChatExtensionProtocolEvent cepe = (ChatExtensionProtocolEvent)event;
   if(cepe.getExtensionName().equals("GET_USER_INFO")){
    HashMap<String, String> param = new HashMap<String, String>();
	param.put("OS_NAME", System.getProperty("os.name"));
	param.put("OS_VER", System.getProperty("os.version"));
	chat.openChat(cepe.getFrom());
	chat.sendExtensionProtocolMessage(cepe.getFrom(),
			"USER_INFO", param);
	chat.closeChat(cepe.getFrom());
   }
   if(cepe.getExtensionName().equals("USER_INFO")){
	String osName = (String) cepe.getExtensionParameter("OS_NAME");
	String osVer = (String) cepe.getExtensionParameter("OS_VER");
	showMessage("One2OneInfo plugin", "OS: " + osName + " ver: " + osVer);
	chat.closeChat(cepe.getFrom());
   }
  }
 }
 
 private void showMessage(String windowTitle, String message) {
	UiPlugin.getUIHelper().showMessage(windowTitle, message);
 }
}
```

Questa classe estende l'interfaccia _IBackendEventListener_ e nell'implementazione del metodo _onBackendEvent()_ intercetta due tipi di estensioni al protocollo:
  * GET\_USER\_INFO: un utente ha richiesto le nostre informazioni e noi le inviamo.
  * USER\_INFO: un utente ci ha inviato le sue informazioni che noi abbiamo precedentemente richiesto.
Il protocollo utilizzato è il seguente:

```
Utente1 > > > > > > > > > > [GET_USER_INFO, null] > > > > > > > > > Utente2
Utente1 < < < < < < < < < < < < [USER_INFO, info] < < < < < < < < < < Utente2
```

Abbiamo creato il plugin, abbiamo gestito i messaggi in ingresso ed ora non ci rimane che gestire l'invio della richiesta dall'Action creata all'inizio.
Apriamo la classe _GetUserInfo_ ed inseriamo il seguente codice:

```
public class GetUserInfo extends AbstractBuddyActionDelegate{
 @Override
 public void run(IAction action) {
  final IBuddy buddy = getSelected();
  IBackend b = NetworkPlugin.getDefault().getRegistry().getDefaultBackend();
  IChatServiceActions chat = b.getChatServiceAction();
  chat.openChat(buddy.getId());
  chat.sendExtensionProtocolMessage(buddy.getId(),
		"GET_USER_INFO", new HashMap<String, String>());
 }
}
```

Infine, poiché è di importanza critica che le stringhe rapprensentanti l'estensione del protocollo siano scritte correttamente, è utile rifattorizzare il codice, in modo da raccogliere le stringhe costanti in un unica interfaccia. Così facendo, si eviterà di incorrere in errori e lunghe sessioni di debug a causa di banali errori di battitura.

```
public interface One2OneInfoConstants {
	public static final String GET_USER_INFO = "GET_USER_INFO";
	public static final String USER_INFO =  "USER_INFO";
	public static final String OS_NAME = "OS_NAME";
	public static final String OS_VER = "OS_VER";	
}
```

### Esecuzione del nuovo servizio ###
Questo codice verrà eseguito ogni volta che selezioneremo la voce "Get Info" dal popupMenu del roster. Esso non fa altro che ricevere un'istanza di _IChatServiceActions_ ed inviare all'utente selezionato una richiesta di informazioni.
Abbiamo terminato il nostro servizio!
Ora che è tutto pronto possiamo testare ciò che abbiamo creato. Per fare questo dobbiamo aggiungere il plugin _it.uniba.di.cdg.xcore.one2oneinfo_ tramite il pannello "Run Configurations" e poi avviare l'applicazione.
Se abbiamo eseguito tutti i passi correttamente, dopo aver richiesto le informazioni di un utente, visualizzeremo una schermata simile a quella in Figura 14.

![http://econference4.googlecode.com/svn/wiki/img/extend14a.png](http://econference4.googlecode.com/svn/wiki/img/extend14a.png)
![http://econference4.googlecode.com/svn/wiki/img/extend14b.png](http://econference4.googlecode.com/svn/wiki/img/extend14b.png)<br />
**Figura 14. Test del plugin it.uniba.di.cdg.xcore.one2oneinfo**

In questo esempio è stata utilizzata solo l'interfaccia _IChatServiceActions_, la quale mette a disposizione le funzioni di base di una chat uno ad uno. Tramite _IBackend_ è possibile avere accesso anche a:
  * _IMultichatActions_ che espone metodi per le chat di gruppo;
  * _ICallAction_ che mette a disposizione metodi per la chiamata uno ad uno;
  * _IMultiCallAction_ che mette a disposizione metodi per le chiamate di gruppo.