# Perto de Mim — API Backend

API REST do aplicativo Perto de Mim, desenvolvida em Node.js com Express e PostgreSQL.

---

## Tecnologias

- Node.js com ES Modules
- Express
- PostgreSQL
- bcrypt (senhas)
- JWT (autenticacao)
- dotenv (variaveis de ambiente)

---

## Como rodar o projeto

### Pre-requisitos
- Node.js instalado
- PostgreSQL instalado e rodando

### Configuracao

1. Clone o repositorio:
```bash
git clone https://github.com/marinacpontes/PertoDeMim.git
```

2. Instale as dependencias:
```bash
npm install
```

3. Crie o arquivo `.env` na raiz do projeto com as variaveis:
```
DB_USER=seu_usuario_postgres
DB_HOST=localhost
DB_NAME=perto_de_mim
DB_PASSWORD=sua_senha
DB_PORT=5432
JWT_SECRET=uma_chave_secreta_longa
```

> **Importante:** O usuario do PostgreSQL varia conforme o sistema. No Mac geralmente e o nome do usuario do sistema (ex: `juliaduran`). No Windows geralmente e `postgres`.

4. Rode o servidor:
```bash
node src/app.js
```

O servidor sobe em `http://localhost:3000`

---

## Estrutura do projeto

```
backend-app/
├── src/
│   ├── config/
│   │   └── db.js
│   ├── controllers/
│   │   ├── usuariosController.js
│   │   ├── fornecedoresController.js
│   │   ├── servicosController.js
│   │   ├── pedidosController.js
│   │   ├── avaliacoesController.js
│   │   └── authController.js
│   ├── middlewares/
│   │   └── authMiddleware.js
│   ├── routes/
│   │   ├── usuarios.js
│   │   ├── fornecedores.js
│   │   ├── servicos.js
│   │   ├── pedidos.js
│   │   ├── avaliacoes.js
│   │   └── auth.js
│   └── app.js
├── .env                  # NAO sobe no Git
├── .env.example
├── .gitignore
└── package.json
```

---

## Banco de dados — tabelas

```
usuarios
clientes
fornecedores
categorias
servicos
pedidos
avaliacoes
favoritos
pagamentos
mensagens
portifolio
```

---

## Autenticacao

Rotas protegidas exigem token JWT no header:

```
Authorization: Bearer <token>
```

O token e obtido no login e expira em 7 dias.

---

## Rotas

### 1. Usuarios

#### POST `/usuarios` — Criar cliente

**Body:**
```json
{
  "nome": "Julia",
  "email": "julia@email.com",
  "senha": "123456",
  "tipo": "cliente",
  "telefone": "85999999999",
  "cpf_cnpj": "52998224725",
  "data_nascimento": "1999-05-15",
  "logradouro": "Rua das Flores",
  "cep": "60000000",
  "numero": "123",
  "bairro": "Meireles",
  "complemento": "Apto 42",
  "cidade": "Fortaleza",
  "estado": "CE"
}
```

**Regras de validacao:**
| Campo | Regra |
|---|---|
| nome | Obrigatorio, max 40 caracteres |
| email | Obrigatorio, formato valido. Nao pode ser duplicado |
| senha | Obrigatorio, minimo 6 caracteres |
| tipo | Obrigatorio: `"cliente"` ou `"fornecedor"` |
| telefone | Obrigatorio, DDD valido + numero (so digitos, 10 ou 11) |
| cpf_cnpj | Obrigatorio, CPF (11 digitos) ou CNPJ (14 digitos). Nao pode ser duplicado |
| data_nascimento | Obrigatorio, formato `YYYY-MM-DD`. Cliente: min 16 anos. Fornecedor: min 18 anos |
| logradouro | Obrigatorio, max 50 caracteres |
| cep | Obrigatorio, 8 digitos |
| numero | Obrigatorio |
| bairro | Obrigatorio |
| cidade | Obrigatorio |
| estado | Obrigatorio, sigla ex: `"CE"` |
| complemento | Opcional |

`201` — usuario criado | `400` — erro de validacao | `409` — duplicidade

---

#### GET `/usuarios` — Listar todos
#### GET `/usuarios/:id` — Buscar por ID

---

### 2. Fornecedores

#### POST `/fornecedores` — Cadastrar fornecedor

**Body:**
```json
{
  "nome": "Marina Silva",
  "email": "marina@email.com",
  "senha": "123456",
  "telefone": "85988887777",
  "cpf_cnpj": "11144477735",
  "data_nascimento": "1995-03-20",
  "logradouro": "Av. Beira Mar",
  "cep": "60165121",
  "numero": "100",
  "bairro": "Meireles",
  "cidade": "Fortaleza",
  "estado": "CE",
  "nome_loja": "Studio Marina",
  "nome_responsavel": "Marina Silva",
  "categoria": "Beleza e Estetica",
  "descricao": "Especialista em cabelo e maquiagem",
  "preco_medio": 80
}
```

> Quando `categoria` for `"Outros"`, adicionar `"categoria_outro": "Tatuagem"`.

**Categorias validas:**
`Beleza e Estetica` | `Saude` | `Alimentacao` | `Manutencao` | `Tecnologia` | `Outros`

`201` — fornecedor cadastrado | `400` — erro de validacao | `409` — duplicidade

---

#### GET `/fornecedores` — Listar todos
#### GET `/fornecedores/:id` — Buscar por ID

---

### 3. Servicos

#### POST `/servicos` — Cadastrar servico
**Rota protegida — token de fornecedor**

**Body:**
```json
{
  "nome": "Corte de cabelo",
  "descricao": "Corte feminino com lavagem e escova",
  "preco": 80,
  "categoria_id": 1
}
```

**Categorias (IDs):**
| ID | Nome |
|---|---|
| 1 | Beleza e Estetica |
| 2 | Saude |
| 3 | Alimentacao |
| 4 | Manutencao |
| 5 | Tecnologia |
| 6 | Outros |

`201` — servico cadastrado | `400` — erro de validacao | `403` — usuario nao e fornecedor

---

#### GET `/servicos` — Listar todos (publico)
#### GET `/servicos/:id` — Buscar por ID (publico)
#### GET `/servicos/fornecedor/:id` — Listar servicos de um fornecedor (publico)

---

### 4. Pedidos

#### POST `/pedidos` — Criar pedido
**Rota protegida — token de cliente**

**Body:**
```json
{
  "servico_id": 1
}
```

**Regras de status por tipo de usuario:**
| Tipo | Status permitidos |
|---|---|
| fornecedor | `aceito`, `recusado`, `concluido` |
| cliente | `cancelado` |

> Pedidos com status `concluido`, `cancelado` ou `recusado` nao podem ser alterados.

---

#### GET `/pedidos/meus` — Listar pedidos do usuario logado (protegida)
#### GET `/pedidos/:id` — Buscar pedido por ID (protegida)

---

#### PATCH `/pedidos/:id/status` — Atualizar status
**Rota protegida**

**Body:**
```json
{
  "status": "aceito"
}
```

---

### 5. Avaliacoes

#### POST `/avaliacoes` — Criar avaliacao
**Rota protegida — token de cliente**

Regras:
- So pode avaliar pedidos com status `concluido`
- 1 avaliacao por pedido
- Nota de 1 a 5 estrelas
- Comentario e opcional

**Body:**
```json
{
  "pedido_id": 1,
  "nota": 5,
  "comentario": "Excelente servico!"
}
```

**Resposta `201`:**
```json
{
  "mensagem": "Avaliacao registrada com sucesso!",
  "avaliacao": {
    "id": 1,
    "cliente_id": 1,
    "servico_id": 1,
    "pedido_id": 1,
    "nota": 5,
    "comentario": "Excelente servico!",
    "created_at": "2026-05-13T00:03:23.432Z"
  }
}
```

`400` — pedido nao concluido:
```json
{
  "erro": "So e possivel avaliar pedidos com status \"concluido\"."
}
```

`409` — pedido ja avaliado:
```json
{
  "erro": "Este pedido ja foi avaliado."
}
```

---

#### GET `/avaliacoes/servico/:id` — Avaliacoes de um servico (publico)

Retorna lista de avaliacoes com media de notas.

**Resposta `200`:**
```json
{
  "servico_id": 1,
  "total_avaliacoes": 1,
  "media_nota": 5,
  "avaliacoes": [
    {
      "id": 1,
      "nota": 5,
      "comentario": "Excelente servico!",
      "nome_cliente": "Julia Cliente",
      "servico": "Corte de cabelo"
    }
  ]
}
```

---

#### GET `/avaliacoes/fornecedor/:id` — Avaliacoes de um fornecedor (publico)

Retorna media geral e todas as avaliacoes do fornecedor.

**Resposta `200`:**
```json
{
  "fornecedor_usuario_id": 1,
  "total_avaliacoes": 1,
  "media_geral": 5,
  "avaliacoes": [ ... ]
}
```

---

### 6. Autenticacao

#### POST `/auth/login` — Login

**Body:**
```json
{
  "email": "julia@email.com",
  "senha": "123456"
}
```

**Resposta `200`:**
```json
{
  "mensagem": "Login realizado com sucesso!",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "nome": "Julia",
    "email": "julia@email.com",
    "tipo": "cliente"
  }
}
```

`401` — credenciais invalidas.

---

## Observacoes para o front

1. **Mascaras:** aplicar mascara visualmente mas remover antes de enviar.
2. **Data de nascimento:** enviar no formato `YYYY-MM-DD`.
3. **Tipo:** usar o campo `tipo` retornado no login para redirecionar.
4. **Token expirado:** se a API retornar `401` em rota protegida, redirecionar para o login.
5. **Erros:** a API retorna todos os erros de uma vez no array `erros`.
6. **Idade minima:** cliente 16 anos, fornecedor 18 anos.
7. **Categoria Outros:** exibir campo de texto e enviar `categoria_outro`.
8. **Servicos:** para cadastrar precisa de token. Para listar nao precisa.
9. **Pedidos:** o valor e copiado automaticamente do servico.
10. **Status do pedido:** fornecedor pode aceitar, recusar ou concluir. Cliente so pode cancelar.
11. **Avaliacoes:** so apos pedido concluido. Nota de 1 a 5 — mapear estrelas para numeros.
12. **Emulador Android:** usar `http://10.0.2.2:3000` em vez de `http://localhost:3000`.