# Pos Digital

## Pagamento

Para realizar pagamentos será preciso invocar deeplinks. Veja mais detalhes no ![PDF](https://github.com/GETNETBrasil/posdigital-example/tree/master/payment/DOCS/api_pagamento_getnet.pdf).
O ![app](https://github.com/GETNETBrasil/posdigital-example/tree/master/payment/DOCS/Rebatedor-v2.2.0.apk) simula as respostas do app de Pagamento então você precisa instalar este app no terminal durante o desenvolvimento, assim você não irá precisar dos cartões de teste.

## O que você precisa saber antes de começar a desenvolver

1. O desenvolvimento do aplicativo pode ser nativo ou híbrido (Exemplo: Xamarin, porque o
Xamarin irá compilar o código para o “Android Runtime (ART)” como se fosse um app
nativo)
2. É expressamente proibido WebApps e WebView devido a medidas de segurança
3. O GooglePlayService não estará disponível
4. Suporte ao Android 5.1 (22)
5. É de responsabilidade do desenvolvedor garantir a segurança da informação circulada
dentro do aplicativo.

## Boas práticas

1. Uso consciente do consumo de dados
2. Uso consciente do consumo de bobina de impressão
3. Uso consciente do consumo de memória e cpu
4. Utilização das boas práticas de programação Android
5. Evite funcionalidades com timeout infinito que possa deixar sua aplicação travada
aguardando algum evento.
6. Utilizar layout dinâmicos considerando que no futuro o app pode ser instalado em outros
hardwares com resolução e densidade diferentes.
7. Tamanho do APK
8. Evite expor dados sensíveis dos clientes, tais como, senhas, informações pessoais...

# SDK

O modulo SDK é um exemplo de integração entre o app e o hardware. Para utilizar os componentes do terminal (ex.: impressora) basta utilizar os serviços disponibilizados dentro do SDK através da classe ```PosDigital```.

## Configurar Gradle

Esta disponibilizado na pasta sdk/libs uma .aar. Basta importar este .aar no Gradle e a PosDigital está disponível para uso.

No gradle do seu app adicione essa linha:
```
android {
  compileSdkVersion 27
  defaultConfig {
    minSdkVersion 22
    targetSdkVersion 27
  }
}

compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
}

dependencies {
  implementation fileTree(dir: 'libs', include: ['*.aar'])
}
```

## Services

1. Card
2. Impressora
3. Mifare
4. Leds
5. Beeper
6. Camera

## Fazendo bind no service

Adicione no AndroidManifest.xml está permissão

```xml
<uses-permission android:name="com.getnet.posdigital.service.POSDIGITAL" />
```

A classe PosDigital possui métodos que auxiliam no bind do serviço:

```java
PosDigital.register(getApplicationContext(), new PosDigital.BindCallback(){

  @Override
  public void onError(Exception e) {

  }

  @Override
  public void onDisconnected() {

  }

  @Override
  public void onConnected() {

  }

});
```

Somente após o método ```onConnected``` ser executado que os serviços irão funcionar. Para testar se o serviço está conectado execute o método```PosDigital.getInstance().isInitiated() ```

## Card

Para utilizar os eventos de cartões basta chamar o método ```PosDigital.getInstance().getCard() ``` e estão disponíveis as funções das leitoras.

Há alguns exemplos de uso na classe ```CardActivity```

Exemplo
```java
int timeout = 30; // Segundos
int[] searchType = {SearchType.MAG, SearchType.CHIP, SearchType.NFC};
PosDigital.getInstance().getCard().search(timeout, searchType, new ICardCallback.Stub(){

  @Override
  public void onCard(CardResponse cardResponse) {

  }

  @Override
  public void onMessage(String message) {

  }

  @Override
  public void onError(String error) {

  }

});
```

## Impressora

Para utilizar a impressora basta chamar o método ```PosDigital.getInstance().getPrinter() ``` e estão disponíveis as funções da impressora.

Há alguns exemplos de uso na classe ```PrinterActivity```

Exemplo:
```java
PosDigital.getInstance().getPrinter().init();
PosDigital.getInstance().getPrinter().setGray(5);
PosDigital.getInstance().getPrinter().defineFontFormat(FontFormat.MEDIUM);
PosDigital.getInstance().getPrinter().addText(AlignMode.LEFT, "EXEMPLO DE TEXTO");
PosDigital.getInstance().getPrinter().print(new IPrinterCallback.Stub() {
 @Override
 public void onSuccess() {
  // Imprimiu o texto
 }

 @Override
 public void onError(int cause) {
    // Ocorreu Algum erro
 }
});
```

O tratamento de erros da impressão pode ser feito com o auxilio das constantes da classe ```PrinterStatus```

```java
PrinterStatus.OK -> "OK"
PrinterStatus.PRINTING -> "Imprimindo"
PrinterStatus.ERROR_NOT_INIT -> "Impressora não iniciada"
PrinterStatus.ERROR_OVERHEAT -> "Impressora superaquecida"
PrinterStatus.ERROR_BUFOVERFLOW -> "Fila de impressão muito grande"
PrinterStatus.ERROR_PARAM -> "Parametros incorretos"
PrinterStatus.ERROR_LIFTHEAD -> "Porta da impressora aberta"
PrinterStatus.ERROR_LOWTEMP -> "Temperatura baixa demais para impressão"
PrinterStatus.ERROR_LOWVOL -> "Sem bateria suficiente para impressão"
PrinterStatus.ERROR_MOTORERR -> "Motor de passo com problemas"
PrinterStatus.ERROR_NO_PAPER -> "Sem bonina"
PrinterStatus.ERROR_PAPERENDING -> "Bobina acabando"
PrinterStatus.ERROR_PAPERJAM -> "Bobina travada"
PrinterStatus.UNKNOW -> "Não foi possível definir o erro"
```

## Mifare

Para utilizar o mifare basta chamar o método ```PosDigital.getInstance().getMifare() ``` e estão disponíveis as funções do mifare.

Há alguns exemplos de uso na classe ```MifareActivity```

## Leds

Para ligar e desligar os Leds basta chamar o método ```PosDigital.getInstance().getLeds() ``` e estarão disponíveis as funções dos lds.

```java
 PosDigital.getInstance().getLed().turnOnAll()
 PosDigital.getInstance().getLed().turnOffAll()
```

Há alguns exemplos de uso na classe ```LedActivity```


## Beeper

Para utilizar o beeper basta chamar o método ```PosDigital.getInstance().getBeeper() ``` e estão disponíveis as funções do beeper.

Há alguns exemplos de uso na classe ```BeeperActivity```

```java
  PosDigital.getInstance().getBeeper().success()
```

## Camera

Para utilizar a camera basta chamar o método ```PosDigital.getInstance().getCamera() ``` e estão disponíveis as funções da camera.

Há alguns exemplos de uso na classe ```CameraActivity```

```java
  int timeout = 30 // segundos
  PosDigital.getInstance().getCamera().readBack(timeout, new ICameraCallback.Stub(){

    @Override
    public void onSuccess(String code){

    }

    @Override
    public void onTimeout(){

    }

    @Override
    public void onCancel(){

    }

    @Override
    public void onError(String error) {
    }

  });
```
