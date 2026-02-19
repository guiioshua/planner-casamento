# Product Requirements Document (PRD) - Planner de Casamento (MVP)

## 1. Introdução e Objetivo
O objetivo deste projeto é desenvolver uma aplicação web simples e efetiva para o gerenciamento de tarefas e convidados de um casamento. O foco do MVP (Minimum Viable Product) é a funcionalidade central sem barreiras de entrada complexas (como sistemas de autenticação robustos), permitindo que os noivos gerenciem convites, confirmações de presença (RSVP), lista de presentes e despesas com fornecedores em uma interface unificada.

## 2. Escopo do Produto
O sistema operará em dois contextos distintos:
1.  **Visão Administrativa (Noivos):** Gerenciamento de convites, padrinhos, presentes e financeiro.
2.  **Visão Pública (Convidados):** Interface de RSVP e visualização/interação com a lista de presentes, acessível via link único (hash).

## 3. Requisitos Funcionais (RF)

### 3.1. Módulo de Gestão de Convites e Convidados
**RF-01: Criação de Convites (Entidade Agrupadora)**
* O sistema deve permitir criar um "Convite".
* Um convite agrupa uma ou mais pessoas (ex: Família Silva).
* **Campos:** Nome do Convite (identificador amigável), Tipo (Convite Padrão ou Padrinhos).

**RF-02: Adição de Pessoas ao Convite**
* Deve ser possível adicionar múltiplas pessoas a um único Convite.
* **Campos:** Nome Completo, Telefone (WhatsApp).
* **Status Individual:** Cada pessoa dentro do convite deve ter um status: `Pendente` (padrão), `Confirmado`, `Não Irá`.

**RF-03: Geração de Link Único e RSVP**
* O sistema deve gerar um *slug* ou *hash* único (UUID) para cada Convite criado.
* Ao acessar `app.com/rsvp/{hash-do-convite}`, o usuário vê a interface de confirmação.
* **Interface de RSVP:** Deve exibir a imagem do convite (configurável pelo admin), a mensagem personalizada e a lista de pessoas daquele convite.
* O convidado deve poder marcar "Sim" ou "Não" para cada pessoa listada no convite.
* Ao salvar, o status no banco de dados é atualizado automaticamente.
* O mesmo convite pode ser acessado multiplas vezes pelos convidados para possíveis atualizações de RVSP, passando pelo mesmo fluxo de responder e visualizar tela de presentes

**RF-04: Customização Visual do Convite**
* O admin deve poder fornecer uma imagem de capa para o convite via **upload de arquivo** ou **colagem direta (Ctrl+V)** na área designada. Campo de URL de imagem não deve ser exposto ao usuário.
* O admin deve poder escrever um texto personalizado que aparecerá na tela de RSVP.
* Diferenciação: Deve haver uma configuração visual ou fluxo separado para "Padrinhos" (imagem e texto distintos dos convites comuns).

**RF-05: Envio (Manual)**
* O sistema não envia mensagens automaticamente, mas deve fornecer um botão "Copiar Link" ou "Enviar via WhatsApp" que abre a API do WhatsApp (`wa.me`) com uma mensagem pré-definida contendo o link do convite para o número da primeira pessoa cadastrada naquele convite, se disponível.

### 3.2. Módulo de Padrinhos (Especialização)
**RF-06: Segregação de Padrinhos**
* O sistema deve permitir filtrar ou visualizar separadamente a lista de Padrinhos.
* A lógica de confirmação é idêntica à dos convites comuns, mas os dados devem ser contabilizados em dashboards/listas separadas para facilitar a organização.

### 3.3. Módulo de Lista de Presentes
**RF-07: Cadastro de Presentes**
* O admin adiciona itens à lista de desejos.
* **Campos:** Nome do Presente, Link de Compra (URL externa).
* *Imagem do Presente:* A imagem do presente deve ser inputada pelo admin através de um link.
* O admin pode **desabilitar** um presente (ocultá-lo da lista pública) ou **excluí-lo permanentemente**. Ambas as ações devem estar disponíveis.
* Cada presente possui um **status de disponibilidade**: `Livre` (disponível para escolha) ou `Escolhido` (reservado por um convidado).
* Os convidados devem poder visualizar os presentes já escolhidos mas terem indicação visual do status, mas não devem visualizar os desabilitados.

**RF-08: Visualização e Interação pelo Convidado**
* Após o convidado confirmar presença (Status = `Confirmado`) na tela de RSVP, o sistema deve exibir um botão ou seção "Ver Lista de Presentes".
* Se o convidado acessar o link do convite novamente e já estiver confirmado, a lista deve estar visível imediatamente.
* Na lista de presentes, cada item exibe seu status: **Livre** ou **Escolhido**.
* O convidado pode **marcar um presente como "Escolhido"**, sinalizando sua intenção de presentear com aquele item. Após marcado, o presente passa a aparecer como indisponível para os demais convidados.

### 3.4. Módulo Financeiro (CRM de Fornecedores)
**RF-09: Gestão de Prestadores de Serviço**
* CRUD (Create, Read, Update, Delete) de fornecedores.
* **Campos:** Nome da Empresa, Tipo de Serviço (ex: Buffet, Foto, Som), Nome do Contato, Telefone/WhatsApp, Valor Total, Valor Pago (opcional para cálculo de saldo), Notas/Observações.

**RF-10: Totais Financeiros**
* Definição/edição de Total Orçado.
* Exibição simples do somatório dos valores dos serviços cadastrados para controle orçamentário.

## 4. Estrutura de Dados (Sugestão de Schema)

Para orientar a I.A. na criação do banco de dados (SQL ou NoSQL), utilize as seguintes entidades:

### Tabela: `invitations`
* `id`: UUID (Primary Key)
* `family_name`: String (ex: "Família Souza")
* `type`: Enum (`standard`, `godparent`)
* `slug`: String (Unique Index - usado na URL)
* `cover_image_url`: String (referência interna ao arquivo armazenado — não exposta como campo de texto ao usuário)
* `message_body`: Text
* `created_at`: DateTime

### Tabela: `guests`
* `id`: UUID (Primary Key)
* `invitation_id`: UUID (Foreign Key -> invitations.id)
* `full_name`: String
* `phone`: String
* `status`: Enum (`pending`, `confirmed`, `declined`)

### Tabela: `gifts`
* `id`: UUID (Primary Key)
* `name`: String
* `purchase_link`: String
* `image_url`: String
* `status`: Enum (`available`, `chosen`) — **substitui o campo `is_active` booleano**
* `is_visible`: Boolean (controla se o presente aparece na lista pública; substitui o uso de `is_active` para visibilidade)

### Tabela: `vendors`
* `id`: UUID (Primary Key)
* `company_name`: String
* `service_category`: String
* `contact_name`: String
* `contact_phone`: String
* `price`: Decimal
* `amount_paid`: Decimal (opcional)
* `notes`: Text

## 5. Requisitos Não Funcionais e Técnicos

1.  **Arquitetura:** Frontend e Backend desacoplados.
2.  **Persistência:** Banco de dados relacional (PostgreSQL).
3.  **Upload de Imagens:** As imagens de capa dos convites são fornecidas exclusivamente via upload de arquivo ou colagem (Ctrl+V). Para o MVP, podem ser armazenadas em um bucket de armazenamento simples (AWS S3 / Supabase Storage) ou em base64 se pequenas.
4.  **Interface:** Design responsivo (Mobile First), pois os convidados acessarão o RSVP via celular.

## 6. Fluxos de Usuário (User Flows)

### Fluxo 1: Admin Cria Convite
1.  Admin acessa painel "Convites".
2.  Clica em "Novo Convite".
3.  Define se é Padrinho ou Convidado.
4.  Adiciona Nomes e Telefones dos integrantes.
5.  Faz upload da imagem de capa via seleção de arquivo ou colagem (Ctrl+V).
6.  Sistema gera Link.
7.  Admin envia link via WhatsApp.

### Fluxo 2: Convidado Confirma Presença
1.  Convidado clica no link recebido.
2.  Visualiza "Capa" e "Mensagem".
3.  Visualiza lista com seu nome e familiares.
4.  Alterna *switch* para "Vou" ou "Não vou" em cada nome.
5.  Clica em "Confirmar".
6.  Sistema exibe modal/sucesso e libera botão "Lista de Presentes".

### Fluxo 3: Convidado Interage com Lista de Presentes
1.  Convidado confirmado acessa a lista de presentes.
2.  Visualiza itens com status **Livre** ou **Escolhido**.
3.  Clica em um presente com status **Livre**.
4.  Sistema atualiza o status do presente para **Escolhido**.
5.  Presente passa a aparecer como indisponível para os demais convidados.

### Fluxo 4: Admin Cadastra Presente
1.  Admin acessa painel "Presentes".
2.  Clica em "Novo Presente".
3.  Informa Nome, Imagem (opcional) e Link de Compra.
4.  Admin salva o presente (status inicial: **Livre**, visível na lista pública).
5.  Admin pode a qualquer momento desabilitar (ocultar da lista pública) ou excluir permanentemente o presente.