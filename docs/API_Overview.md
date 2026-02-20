# Planejador de Casamento - Visão Geral da API

Baseado na implementação atual do backend (Controllers & DTOs do Spring Boot).

**URL Base:** `http://localhost:8081/api/v1`

---

## 1. Convites (Invitations)

**Controller:** `InvitationController`
**Caminho Base:** `/invitations`

### Listar Todos os Convites
- **GET** `/invitations`
- **Resposta:** `List<InvitationResponse>` (JSON)

### Obter Convite por Slug
- **GET** `/invitations/slug/{slug}`
- **Resposta:** `InvitationResponse` (JSON)

### Criar Convite
- **POST** `/invitations`
- **Content-Type:** `multipart/form-data`
- **Partes:**
  - `data` (JSON): Esquema `CreateInvitationRequest`
  - `coverImage` (Arquivo, Opcional): Arquivo de imagem para a capa.
- **Comportamento:**
  - Se o arquivo `coverImage` for fornecido, ele é carregado e sua URL é usada.
  - Gera um `slug` e `id` únicos.
  - Cria os `convidados` associados.

### Atualizar Convite
- **PUT** `/invitations/{id}`
- **Content-Type:** `multipart/form-data`
- **Partes:**
  - `data` (JSON): Esquema `CreateInvitationRequest`
  - `coverImage` (Arquivo, Opcional): Novo arquivo de imagem.
- **Comportamento:**
  - Atualiza os detalhes do convite.
  - **Aviso:** Atualmente substitui **TODOS** os convidados (remove os antigos, cria novos com novos IDs).

### Excluir Convite
- **DELETE** `/invitations/{id}`
- **Resposta:** `204 No Content`

#### Estruturas de Dados

**`CreateInvitationRequest` (parte JSON):**
```json
{
  "familyName": "Família Souza",
  "type": "STANDARD",        // Enum: STANDARD, GODPARENT
  "messageBody": "Venha celebrar conosco!",
  "coverImageUrl": "http://...", // Opcional: A lógica prefere o arquivo enviado, se presente
  "categories": ["A", "B"], // Lista de categorias às quais este convite pertence
  "guests": [
    {
      "fullName": "João Souza",
      "phone": "11999999999",
      "status": "PENDING",      // Opcional (padrão PENDING, Enum: PENDING, CONFIRMED, DECLINED)
      "isChild": false          // Booleano — deve usar a chave "isChild" (não "child")
    }
  ]
}
```

**`InvitationResponse`:**
```json
{
  "id": "uuid",
  "familyName": "Família Souza",
  "type": "STANDARD",
  "slug": "slug-unico-string",
  "coverImageUrl": "http://localhost:8081/uploads/...",
  "messageBody": "...",
  "createdAt": "2024-01-01T10:00:00Z",
  "categories": ["A", "B"],
  "guests": [
    {
      "id": "uuid",
      "fullName": "João Souza",
      "phone": "...",
      "status": "PENDING",
      "isChild": false
    }
  ]
}
```

---

## 2. RSVP (Acesso Público)

**Controller:** `RsvpController`
**Caminho Base:** `/rsvp`

### Obter Dados Públicos do Convite
- **GET** `/rsvp/{slug}`
- **Resposta:** `InvitationResponse` (Igual ao acima)
- **Caso de Uso:** Página de destino pública para o convidado ver seu convite.

### Confirmar/Recusar Presença
- **POST** `/rsvp/{slug}/confirm`
- **Corpo:** `RsvpUpdateRequest` (JSON)
- **Resposta:** `InvitationResponse` (Atualizada)

#### Estruturas de Dados

**`RsvpUpdateRequest`:**
```json
{
  "statuses": {
    "guest-uuid-1": "CONFIRMED",
    "guest-uuid-2": "DECLINED"
  }
}
```
*Status Válidos:* `PENDING`, `CONFIRMED`, `DECLINED`

---

## 3. Presentes (Lista de Presentes)

**Controller:** `GiftController`
**Caminho Base:** `/gifts`

### Listar Todos os Presentes (Admin)
- **GET** `/gifts`
- **Resposta:** `List<GiftResponse>` (JSON)

### Listar Presentes Visíveis (Público)
- **GET** `/gifts/visible`
- **Resposta:** `List<GiftResponse>` (JSON)
- **Comportamento:** Retorna todos os presentes onde `visible = true`. Inclui o status (`AVAILABLE` ou `CHOSEN`).

### Criar Presente
- **POST** `/gifts`
- **Corpo:** `GiftRequest` (JSON)
- **Resposta:** `GiftResponse`

### Atualizar Presente
- **PUT** `/gifts/{id}`
- **Corpo:** `GiftRequest` (JSON)
- **Resposta:** `GiftResponse`

### Marcar como Escolhido (Ação do Convidado)
- **PATCH** `/gifts/{id}/choose`
- **Corpo:** `ChooseGiftRequest` (JSON)
- **Resposta:** `GiftResponse`
- **Comportamento:**
  - Altera o status para `CHOSEN`.
  - Vincula o presente à família do convite (através do `invitationSlug`).
  - Lança erro se o presente já tiver sido escolhido.

### Excluir Presente
- **DELETE** `/gifts/{id}`
- **Resposta:** `204 No Content`

#### Estruturas de Dados

**`GiftRequest`:**
```json
{
  "name": "Micro-ondas",
  "purchaseLink": "https://amazon...",
  "imageUrl": "https://...",
  "visible": true,       // Opcional (padrão true)
  "category": "A",       // Categoria para filtragem (corresponde às categorias do convite)
  "status": "AVAILABLE"  // Opcional (padrão AVAILABLE, Enum: AVAILABLE, CHOSEN)
}
```

**`ChooseGiftRequest`:**
```json
{
  "invitationSlug": "familia-souza-abc123" // Slug do convite do convidado; vincula o presente à família
}
```

**`GiftResponse`:**
```json
{
  "id": "uuid",
  "name": "Micro-ondas",
  "purchaseLink": "...",
  "imageUrl": "...",
  "status": "AVAILABLE",
  "visible": true,
  "category": "A",
  "chosenByFamilyName": "Família Souza" // Nulo quando o status é AVAILABLE
}
```

> **Nota:** O campo `isChild` nos DTOs de convidados é anotado com `@JsonProperty("isChild")` no backend para evitar que o Jackson remova o prefixo `is` (que, de outra forma, o exporia como `"child"` no JSON).

---

## 4. Fornecedores (Admin)

**Controller:** `VendorController`
**Caminho Base:** `/vendors`

### Operações CRUD
- **GET** `/vendors` → Listar todos
- **POST** `/vendors` → Criar
- **PUT** `/vendors/{id}` → Atualizar
- **DELETE** `/vendors/{id}` → Excluir

#### Estruturas de Dados

**`VendorRequest`:**
```json
{
  "companyName": "Buffet X",
  "serviceCategory": "Buffet",
  "contactName": "Maria",
  "contactPhone": "11988888888",
  "price": 5000.00,
  "amountPaid": 1500.00,
  "notes": "Pagar restante até o dia 10"
}
```

**`VendorResponse`:** Mesmos campos da requisição + `id` (UUID).
