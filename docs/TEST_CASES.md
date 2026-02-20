# Guia Abrangente de Casos de Teste

Este documento descreve os cenários de teste para a aplicação "Planner de Casamento", cobrindo as funcionalidades principais para os papéis de Administrador (Noivos) e Convidado, bem como casos de borda.

## 1. Módulo Administrativo (Noivos)

### 1.1. Gerenciar Convites (Invitations)
**Objetivo:** Garantir que os convites sejam criados, atualizados e excluídos corretamente com todos os dados associados (convidados, categorias, imagem de capa).

| ID | Cenário de Teste | Pré-condições | Passos | Resultado Esperado |
| :--- | :--- | :--- | :--- | :--- |
| **TC-ADM-01** | **Criar Convite Padrão** | Admin logado | 1. Ir para "Convites".<br>2. Clicar em "Novo Convite".<br>3. Preencher Nome da Família: "Família Silva".<br>4. Selecionar Tipo: "Padrão".<br>5. Adicionar Convidado: "João Silva", Telefone: "11999999999".<br>6. **Inserir Categorias:** "A", "B".<br>7. Clicar em "Criar". | Convite criado com sucesso. Aparece na lista. Categorias "A" e "B" são salvas. Slug da URL é gerado. |
| **TC-ADM-02** | **Criar Convite de Padrinho** | Admin logado | 1. Ir para "Convites".<br>2. Clicar em "Novo Convite".<br>3. Selecionar Tipo: "Padrinhos".<br>4. Adicionar Convidados.<br>5. Fazer upload de Imagem de Capa personalizada.<br>6. Clicar em "Criar". | Convite criado com o emblema "Padrinhos". Imagem de capa personalizada é exibida na pré-visualização. |
| **TC-ADM-03** | **Atualizar Categorias de Convite** | Convite existente | 1. Editar um convite existente.<br>2. Alterar Categorias de ["A"] para ["C"].<br>3. Salvar. | Convite atualizado. Convidados que usam este convite devem agora ver APENAS presentes na categoria "C". |
| **TC-ADM-04** | **Adicionar Convidado a Convite Existente** | Convite existente | 1. Editar convite.<br>2. Clicar em "Adicionar Pessoa".<br>3. Preencher detalhes (Nome, Telefone).<br>4. Salvar. | Convidado adicionado ao convite. O total de convidados é atualizado. |
| **TC-ADM-05** | **Excluir Convite** | Convite existente | 1. Clicar em "Excluir" em um convite.<br>2. Confirmar diálogo. | Convite removido da lista. Acessar o SLUG antigo retorna 404 ou página de erro específica. |

### 1.2. Gerenciar Presentes (Gifts)
**Objetivo:** Verificar o gerenciamento de presentes, especificamente a lógica de "Categoria" que define a visibilidade.

| ID | Cenário de Teste | Pré-condições | Passos | Resultado Esperado |
| :--- | :--- | :--- | :--- | :--- |
| **TC-ADM-06** | **Criar Presente com Categoria** | Admin logado | 1. Ir para "Presentes".<br>2. Clicar em "Novo Presente".<br>3. Preencher Nome: "Torradeira".<br>4. **Inserir Categoria:** "A".<br>5. Definir Visível: Verdadeiro.<br>6. Salvar. | Presente criado. Exibe o emblema da categoria "A" no cartão. Status é "Livre" (Disponível). |
| **TC-ADM-07** | **Criar Presente Invisível** | Admin logado | 1. Criar um presente.<br>2. Alternar "Visível" para DESATIVADO.<br>3. Salvar. | Presente criado, mas fica esmaecido na lista do Admin. **NÃO DEVE** aparecer na Interface Pública de Convidados. |
| **TC-ADM-08** | **Editar Categoria de Presente** | Presente "A" existente | 1. Editar presente.<br>2. Alterar Categoria para "B".<br>3. Salvar. | Presente atualizado. Convites com Categoria "A" NÃO devem mais ver este presente. Convites com "B" DEVEM vê-lo. |
| **TC-ADM-09** | **Marcar Presente como Disponível (Reset)** | Presente Escolhido | 1. Encontrar um presente com status "Escolhido".<br>2. Editar e definir o status como "Livre" (se a UI permitir) OU Excluir e Criar novamente. | O presente torna-se disponível para seleção novamente. |

## 2. Módulo Público (Convidados)

### 2.1. Fluxo de RSVP
**Objetivo:** Verificar a experiência do convidado, desde a entrada na página até a confirmação de presença.

| ID | Cenário de Teste | Pré-condições | Passos | Resultado Esperado |
| :--- | :--- | :--- | :--- | :--- |
| **TC-PUB-01** | **Carregar Página de RSVP** | Slug Válido | 1. Acessar `/rsvp/{slug}`. | A página carrega com o Nome da Família, Imagem de Capa e Mensagem corretos. A lista de convidados é exibida. |
| **TC-PUB-02** | **Confirmar Presença** | Página RSVP Carregada | 1. Alternar para "Vou" no Convidado 1.<br>2. Alternar para "Não Vou" no Convidado 2.<br>3. Clicar em "Confirmar Presença". | Mensagem de sucesso aparece. Status atualizados no backend. O botão "Ver Lista de Presentes" aparece. |
| **TC-PUB-03** | **Auto-adicionar Convidado (Acompanhante)** | Página RSVP Carregada | 1. Clicar em "Adicionar Acompanhante".<br>2. Preencher Nome: "Namorada do João".<br>3. Clicar em Salvar.<br>4. Confirmar Presença. | Novo convidado aparece na lista. O status é "Confirmado". O administrador vê este novo convidado no backend. |

### 2.2. Lógica de Presentes Públicos (Caminho Crítico)
**Objetivo:** Validar a lógica de filtragem por categoria projetada para segmentar presentes por nível de convidado.

| ID | Cenário de Teste | Pré-condições | Passos | Resultado Esperado |
| :--- | :--- | :--- | :--- | :--- |
| **TC-PUB-04** | **Acesso Bloqueado para Não Confirmados** | Status Convidado: Pendente | 1. Tentar acessar `/rsvp/{slug}/gifts` diretamente OU procurar o botão. | A página deve redirecionar para o RSVP ou mostrar a mensagem "Confirme sua presença primeiro". O botão deve estar desativado/escondido. |
| **TC-PUB-05** | **Correspondência de Categoria (Positivo)** | Cat. Convite: ["A"]<br>Cat. Presente: "A" | 1. Confirmar presença.<br>2. Ir para a página de Presentes. | Presente "A" **ESTÁ VISÍVEL**. |
| **TC-PUB-06** | **Incompatibilidade de Categoria (Negativo)** | Cat. Convite: ["A"]<br>Cat. Presente: "B" | 1. Confirmar presença.<br>2. Ir para a página de Presentes. | Presente "B" **NÃO ESTÁ VISÍVEL**. |
| **TC-PUB-07** | **Múltiplas Categorias** | Cat. Convite: ["A", "B"] | 1. Ir para a página de Presentes. | O convidado vê **AMBOS** os presentes das categorias "A" e "B". |
| **TC-PUB-08** | **Escolher Presente** | Presente Disponível | 1. Clicar em "Presentear".<br>2. Confirmar. | O botão muda para "Escolhido" (Check verde). Presente marcado como Escolhido no backend. Outros convidados o veem como "Escolhido". |
| **TC-PUB-09** | **Tentar Presente Escolhido** | Presente já Escolhido | 1. Convidado A escolhe Presente X.<br>2. Convidado B acessa a lista. | O convidado B vê o Presente X como "ESCOLHIDO" e o botão está desativado. |

## 3. Casos de Borda e Tratamento de Erros

### 3.1. Casos de Borda Técnicos

| ID | Cenário de Teste | Passos | Resultado Esperado |
| :--- | :--- | :--- | :--- |
| **TC-EDGE-01** | **Slug Inválido** | Acessar `/rsvp/slug-invalido-123`. | **Página 404** ou "Convite não encontrado". Não deve travar. |
| **TC-EDGE-02** | **Categorias Vazias (Convite)** | Criar Convite SEM categorias. | Deve assumir a categoria "A" como padrão (ou padrão do sistema) OU não ver NENHUM presente. Verificar comportamento definido. |
| **TC-EDGE-03** | **Seleção Simultânea de Presente** | Dois convidados tentam escolher o mesmo presente no exato mesmo milissegundo. | Restrição de banco de dados/transação deve evitar reserva dupla. A última requisição deve falhar com erro "Já Escolhido". |
| **TC-EDGE-04** | **Falha de Rede durante RSVP** | Desconectar internet -> Clicar em "Confirmar". | A UI deve mostrar "Erro de conexão" ou "Tente novamente". Não deve congelar. |
| **TC-EDGE-05** | **Injeção de XSS em Formulários** | Inserir `<script>alert(1)</script>` no "Nome da Família" ou "Mensagem". | O sistema deve sanitizar a entrada. O script NÃO deve ser executado quando renderizado. |
| **TC-EDGE-06** | **Upload de Imagem Grande** | Upload de Imagem de Capa de 20MB. | O sistema deve comprimir, rejeitar ou lidar graciosamente, dependendo dos limites. Não deve derrubar o servidor. |

## 4. Checklist de Verificação Manual (Passagem Rápida)

- [ ] **Admin:** Criar Convite (Cat "A") -> Copiar Link.
- [ ] **Admin:** Criar Presente 1 (Cat "A"), Presente 2 (Cat "B").
- [ ] **Convidado:** Abrir Link -> Confirmar Presença.
- [ ] **Convidado:** Abrir Presentes -> Verificar que APENAS o Presente 1 está visível.
- [ ] **Convidado:** Escolher Presente 1 -> Verificar sucesso.
- [ ] **Admin:** Verificar que o status do Presente 1 é "CHOSEN" e está vinculado ao Convite "A".
