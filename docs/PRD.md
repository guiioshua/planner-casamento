# Product Requirements Document (PRD) - Planner de Casamento (MVP)

## 1. Introdução e Objetivo
O objetivo deste projeto é desenvolver uma aplicação web simples e efetiva para o gerenciamento de tarefas e convidados de um casamento. O foco do MVP (Minimum Viable Product) é a funcionalidade central sem barreiras de entrada complexas (como sistemas de autenticação robustos), permitindo que os noivos gerenciem convites, confirmações de presença (RSVP), lista de presentes e despesas com fornecedores em uma interface unificada.

## 2. Escopo do Produto
O sistema operará em dois contextos distintos:
1.  **Visão Administrativa (Noivos):** Gerenciamento de convites, padrinhos, presentes e financeiro.
2.  **Visão Pública (Convidados):** Interface de RSVP e visualização da lista de presentes, acessível via link único (hash).

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

**RF-04: Customização Visual do Convite**
* O admin deve poder fazer upload de uma imagem de capa para o convite.
* O admin deve poder escrever um texto personalizado que aparecerá na tela de RSVP.
* Diferenciação: Deve haver uma configuração visual ou fluxo separado para "Padrinhos" (imagem e texto distintos dos convites comuns).

**RF-05: Envio (Manual)**
* O sistema não envia mensagens automaticamente, mas deve fornecer um botão "Copiar Link" ou "Enviar via WhatsApp" que abre a API do WhatsApp (`wa.me`) com uma mensagem pré-definida contendo o link do convite.

### 3.2. Módulo de Padrinhos (Especialização)
**RF-06: Segregação de Padrinhos**
* O sistema deve permitir filtrar ou visualizar separadamente a lista de Padrinhos.
* A lógica de confirmação é idêntica à dos convites comuns, mas os dados devem ser contabilizados em dashboards/listas separadas para facilitar a organização.

### 3.3. Módulo de Lista de Presentes
**RF-07: Cadastro de Presentes**
* O admin adiciona itens à lista de desejos.
* **Campos:** Nome do Presente, Link de Compra (URL externa), Imagem do Presente (URL ou Upload).
* *Requisito Técnico:* O sistema deve tentar extrair a "Open Graph Image" da URL fornecida para preencher a foto automaticamente. Caso falhe, permite upload manual.

**RF-08: Visualização pelo Convidado**
* Após o convidado confirmar presença (Status = `Confirmado`) na tela de RSVP, o sistema deve exibir um botão ou seção "Ver Lista de Presentes".
* Se o convidado acessar o link do convite novamente e já estiver confirmado, a lista deve estar visível imediatamente.

### 3.4. Módulo Financeiro (CRM de Fornecedores)
**RF-09: Gestão de Prestadores de Serviço**
* CRUD (Create, Read, Update, Delete) de fornecedores.
* **Campos:** Nome da Empresa, Tipo de Serviço (ex: Buffet, Foto, Som), Nome do Contato, Telefone/WhatsApp, Valor Total, Valor Pago (opcional para cálculo de saldo), Notas/Observações.

**RF-10: Totais Financeiros**
* Definição/edição de Total Orçado
* Exibição simples do somatório dos valores dos serviços cadastrados para controle orçamentário.

## 4. Estrutura de Dados (Sugestão de Schema)

Para orientar a I.A. na criação do banco de dados (SQL ou NoSQL), utilize as seguintes entidades:

### Tabela: `invitations`
* `id`: UUID (Primary Key)
* `family_name`: String (ex: "Família Souza")
* `type`: Enum (`standard`, `godparent`)
* `slug`: String (Unique Index - usado na URL)
* `cover_image_url`: String
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
* `is_active`: Boolean

### Tabela: `vendors`
* `id`: UUID (Primary Key)
* `company_name`: String
* `service_category`: String
* `contact_name`: String
* `contact_phone`: String
* `price`: Decimal
* `notes`: Text

## 5. Requisitos Não Funcionais e Técnicos

1.  **Arquitetura:** Frontend e Backend desacoplados ou Monolito modular (ex: Next.js com Server Actions ou Laravel).
2.  **Persistência:** Banco de dados relacional (PostgreSQL ou SQLite para MVP local).
3.  **Upload de Imagens:** Para o MVP, as imagens podem ser armazenadas em base64 (se pequenas) ou em um bucket de armazenamento simples (AWS S3 / Supabase Storage).
4.  **Interface:** Design responsivo (Mobile First), pois os convidados acessarão o RSVP via celular.
5.  **Scraping de Imagem:** Utilizar biblioteca de parser de meta tags para obter a imagem do link do presente.

## 6. Fluxos de Usuário (User Flows)

### Fluxo 1: Admin Cria Convite
1.  Admin acessa painel "Convites".
2.  Clica em "Novo Convite".
3.  Define se é Padrinho ou Convidado.
4.  Adiciona Nomes e Telefones dos integrantes.
5.  Sistema gera Link.
6.  Admin envia link via WhatsApp.

### Fluxo 2: Convidado Confirma Presença
1.  Convidado clica no link recebido.
2.  Visualiza "Capa" e "Mensagem".
3.  Visualiza lista com seu nome e familiares.
4.  Alterna *switch* para "Vou" ou "Não vou" em cada nome.
5.  Clica em "Confirmar".
6.  Sistema exibe modal/sucesso e libera botão "Lista de Presentes".