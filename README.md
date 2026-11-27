# CarTube — YouTube na tela do carro (Android Auto)

App pessoal que mostra o YouTube na tela do carro via Android Auto, com toque
na tela e botões de controle. **O vídeo só aparece com o carro parado**; em
movimento a imagem é coberta e continua só o áudio.

## Gerar o APK (sem instalar nada no PC)

1. Crie um repositório novo no GitHub (ex.: `CarTube`) e envie esta pasta inteira.
2. Abra a aba **Actions** → "Gerar APK". Ele roda sozinho a cada envio
   (ou clique em *Run workflow*).
3. Quando terminar (~5 min), baixe o artefato **CarTube-apk** e extraia o `app-debug.apk`.

Alternativa: abra a pasta no **Android Studio** → *Build → Build APK(s)*.

## Instalar no celular

1. Copie o `app-debug.apk` para o celular e instale (permita "instalar apps desconhecidos").
2. Abra o **CarTube** no celular e **permita o acesso à velocidade do carro**.

## Liberar no Android Auto (uma vez só)

Apps fora da Play Store só aparecem no carro com o modo desenvolvedor:

1. Celular → Configurações → **Android Auto** (ou app Android Auto).
2. Role até **Versão** e toque **10 vezes** → confirme o modo desenvolvedor.
3. Menu ⋮ → **Configurações do desenvolvedor** → marque **Fontes desconhecidas**.
4. Desconecte e reconecte ao carro. O ícone vermelho **CarTube** aparece no menu de apps.
   (Se não aparecer: Android Auto → *Personalizar o menu* → ative o CarTube.)

## Usando no carro

| Controle | O que faz |
|---|---|
| Tocar na tela | Toca na página do YouTube (abrir vídeo, tela cheia etc.) |
| ⟲ | Volta 10 s |
| ▶‖ | Play / pause |
| ⟳ | Avança 10 s |
| 🏠 | Volta para a página inicial do YouTube |
| ✥ (mover) | Ativa a rolagem: arraste o dedo para rolar a página |

**Mandar vídeo do celular:** no app do YouTube → *Compartilhar* → **CarTube**.
O vídeo abre direto no carro.

## Limitações conhecidas

- O login na conta Google não funciona dentro do app (o Google bloqueia login em
  WebView). Dá para assistir e buscar normalmente, sem a conta.
- Se o carro não informar a velocidade, o vídeo fica bloqueado (regra de segurança).
- É um app de uso pessoal: o Google não permite publicar esse tipo de app na Play Store.

## Como funciona (para quem for mexer no código)

- `TubeCarAppService` / `TubeScreen`: app de carro usando o `NavigationTemplate`
  da Car App Library, que dá acesso à superfície inteira da tela.
- `SurfaceRenderer`: cria um `VirtualDisplay` sobre a superfície do carro e
  mostra nele uma `Presentation`.
- `TubePresentation`: `WebView` com `m.youtube.com`; os botões controlam o
  `<video>` da página via JavaScript.
- `SpeedGuard`: lê a velocidade (`CarHardwareManager`) e bloqueia a imagem acima de ~4 km/h.
- `MainActivity` / `PendingLink`: tela do celular e recebimento de links compartilhados.
